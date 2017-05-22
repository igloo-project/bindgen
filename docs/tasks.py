import invoke
from invoke import task
from invoke import Collection

import click
import subprocess
import getpass

import os
import re
import json
try:  # py3
    from shlex import quote
except ImportError:  # py2
    from pipes import quote

CONTEXT_SETTINGS = dict(
    max_content_width=120
)

@click.group(context_settings=CONTEXT_SETTINGS)
def cli():
    pass


@task
def _virtualenv(ctx, virtualenv_path=None):
    """
    Initialize a virtualenv folder.
    """
    virtualenv_path = virtualenv_path or ctx.virtualenv_path
    if not check_virtualenv(ctx, virtualenv_path):
        if not os.path.exists(os.path.dirname(virtualenv_path)):
            os.makedirs(os.path.dirname(virtualenv_path))
        ctx.run(' '.join(['virtualenv', virtualenv_path]))
        if not check_virtualenv(ctx, virtualenv_path):
            raise Exception('python install fails')


@task
def _sphinx(ctx, virtualenv_path=None, skip=None, version=None):
    """
    Install sphinx inside a virtualenv folder.
    """
    skip = skip if (skip is not None) \
        else ctx.sphinx.skip
    dependencies = ctx.sphinx.dependencies
    if not skip:
        virtualenv_path = virtualenv_path or ctx.virtualenv_path
        package = ctx.sphinx.package_name
        version = version or ctx.sphinx.version
        _pip_package(ctx, package, version)
        for dependency in dependencies:
            _pip_package(ctx, dependency['name'],
                         dependency.get('version', None))
    else:
        print('sphinx not managed (sphinx.skip: yes)')


@task
def _recommonmark(ctx, virtualenv_path=None, skip=None, version=None):
    """
    Install recommonmark inside a virtualenv folder.
    """
    skip = skip if (skip is not None) \
        else ctx.recommonmark.skip
    dependencies = ctx.dependencies
    if not skip:
        virtualenv_path = virtualenv_path or ctx.virtualenv_path
        package = ctx.recommonmark.package_name
        version = version or ctx.recommonmark.version
        _pip_package(ctx, package, version)
        for dependency in dependencies:
            _pip_package(ctx, dependency['name'],
                         dependency.get('version', None))
    else:
        print('recommonmark not managed (recommonmark.skip: yes)')


def _pip_package(ctx, package, version=None, virtualenv_path=None):
    """
    Install a pypi package (with pip) inside a virtualenv folder.
    """
    virtualenv_path = virtualenv_path or ctx.virtualenv_path
    if not check_pip(ctx, virtualenv_path, package, version):
        pip_install(ctx, virtualenv_path, package, version)
        if not check_pip(ctx, virtualenv_path, package, version):
            raise Exception('{} install fails'.format(package))


@task(pre=[_virtualenv, _sphinx, _recommonmark])
def configure(ctx):
    """
    Trigger virtualenv and sphinx initialization.

    All this tools are needed to handle documentation generation.
    """
    pass


def _docs_makefile(target, ctx, virtualenv_path=None):
    """
    Trigger a sphinx Makefile target. Used to delegate all documentation jobs to
    original sphinx Makefile.
    """
    virtualenv_path = virtualenv_path or ctx.virtualenv_path
    os.environ['PATH'] = \
        ':'.join([
            os.path.abspath(os.path.join(virtualenv_path, 'bin')),
            os.environ['PATH']])
    args = ['make', '-C', '.', target]
    ctx.run(' '.join(args), pty=True)


@task(pre=[configure])
def docs(ctx, virtualenv_path=None):
    """
    Rebuild documentation.
    """
    _docs_makefile('html', ctx, virtualenv_path)


@task(name='docs-clean', pre=[configure])
def docs_clean(ctx, virtualenv_path=None):
    """
    Clean generated documentation.
    """
    _docs_makefile('clean', ctx, virtualenv_path)


@task(name='docs-live', pre=[docs, configure])
def docs_live(ctx, virtualenv_path=None):
    """
    Live build of documentation on each modification. Open a browser with a
    local server to serve documentation. Opened page is reloaded each time
    documentation is generated.
    """
    virtualenv_path = virtualenv_path or ctx.virtualenv_path
    os.environ['PATH'] = \
        ':'.join([
            os.path.abspath(os.path.join(virtualenv_path, 'bin')),
            os.environ['PATH']])
    command = ' '.join([
        'sphinx-autobuild',
        '-B',
        '--ignore', '"*.swp"',
        '--ignore', '"*.log"',
        '--ignore', '"*~"',
        '--ignore', '"*~"',
        '-b', 'html',
        os.path.dirname(__file__) + '/source',
        os.path.dirname(__file__) + '/build/html'
    ])
    ctx.run(command, pty=True)


def check_virtualenv(ctx, virtualenv_path):
    """
    Check if virtualenv is initialized in virtualenv folder (based on
    bin/python file).
    """
    r = ctx.run(' '.join([
        os.path.join(virtualenv_path, 'bin/python'),
        '--version'
    ]), warn=True, hide='both')
    return r.ok


def check_pip(ctx, virtualenv_path, package, version):
    """
    Check if a pypi package is installed in virtualenv folder.
    """
    r = ctx.run(' '.join([
        os.path.join(virtualenv_path, 'bin/pip'),
        'show',
        package
    ]), hide='both', warn=True)
    if not r.ok:
        # pip show package error - package is not here
        return False
    if version is None:
        # no version check needed
        return True
    # package here, check version
    m = re.search(r'^Version: (.*)$', r.stdout, re.MULTILINE)
    result = m is not None and m.group(1).strip() == version
    return result


def pip_install(ctx, virtualenv_path, package, version):
    """
    Install a pypi package in a virtualenv folder with pip.
    """
    pkgspec = None
    if version is None:
        pkgspec = package
    else:
        pkgspec = '{}=={}'.format(package, version)
    ctx.run(' '.join([
        os.path.join(virtualenv_path, 'bin/pip'),
        'install',
        pkgspec
    ]))


def _vcommand(virtualenv_path, command, *args):
    """
    Run a command from virtualenv folder.
    """
    cl = []
    cl.append(os.path.join(virtualenv_path, 'bin', command))
    cl.extend(args)
    return ' '.join(cl)


def _command(command, *args):
    """
    Run a command.
    """
    cl = []
    cl.append(os.path.join(command))
    cl.extend(args)
    return ' '.join(cl)

ns = Collection(configure,
                docs, docs_live, docs_clean)
ns.configure({
    'sphinx': {
        'package_name': 'sphinx',
        'dependencies': [
            { 'name': 'sphinx-bootstrap-theme' },
            { 'name': 'sphinx-autobuild' }
        ]
    },
    'recommonmark': {
        'package_name': 'recommonmark'
    },
    'dependencies': []
})

if __name__ == '__main__':
    cli()
