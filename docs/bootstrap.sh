#! /bin/bash

set -e

echo -e "\ncheck virtualenv installation\n"
if [ -f /usr/bin/apt-get ]; then
	sudo apt-get install python-virtualenv libssl-dev python-dev gcc
elif [ -f /usr/bin/dnf ]; then
	sudo dnf install python2-virtualenv openssl-devel python-devel gcc
fi

mkdir -p .builddir/
mkdir -p ~/.bin
rm -rf .builddir/invoke
virtualenv .builddir/invoke

echo -e "\ninstall invoke\n"
./.builddir/invoke/bin/pip install invoke
./.builddir/invoke/bin/pip install click==6.0

here=$( readlink -f . )
python=$( readlink -f ./.builddir/invoke/bin/python )

#! /bin/bash

"${python}" "${here}/tasks.py" "\$@"

echo -e "\nsymlink invoke\n"
if [ -h ~/.bin/invoke ]; then
	if [ "$( readlink ~/.bin/invoke )" != "$( readlink -f ./.builddir/invoke/bin/invoke )" ]; then
		echo -e "\n~/.bin/invoke already exists and does not target $( readlink -f ./.builddir/invoke/bin/invoke ) ; install fails\n"
		exit 1
	fi
else
	ln -s "$( readlink -f ./.builddir/invoke/bin/invoke)" ~/.bin/invoke
fi


echo -e "\ninstall done\n"
