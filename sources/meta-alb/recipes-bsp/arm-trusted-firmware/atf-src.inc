URL ?= "git://github.com/nicksungyu/arm-trusted-firmware.git;protocol=ssh"
BRANCH ?= "${RELEASE_BASE}-${PV}"
SRC_URI = "${URL};branch=${BRANCH}"
