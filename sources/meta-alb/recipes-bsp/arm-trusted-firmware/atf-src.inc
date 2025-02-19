URL ?= "git://git@github.com/nicksungyu/arm-trusted-firmware.git;protocol=ssh"
#BRANCH ?= "${RELEASE_BASE}-${PV}"
BRANCH ?= "${RELEASE_BASE}-${PV}_myself"
SRC_URI = "${URL};branch=${BRANCH}"
