#
# Copyright 2017-2024 NXP
#

require recipes-fsl/images/fsl-image-base.bb
require recipes-fsl/images/fsl-image-core-common.inc
include recipes-fsl/images/fsl-image-s32-common.inc

# copy the manifest and the license text for each package to image
COPY_LIC_MANIFEST = "1"
COPY_LIC_DIRS = "1"

IMAGE_INSTALL += " \
    dtc \
    kernel-devicetree \
    packagegroup-core-buildessential \
    packagegroup-core-full-cmdline \
    packagegroup-core-nfs-server \
    packagegroup-core-tools-debug \
    vim \
"

# Benchmark tools
IMAGE_INSTALL += "dhrystone fio"

IMAGE_INSTALL:append:s32cc = " perf"

# PCIe demos
IMAGE_INSTALL:append:s32cc = "${@bb.utils.contains('DISTRO_FEATURES', 'pcie-demos', \
	' kernel-pcitest demo-pcie-shared-mem', '', d)}"


# Userspace support for QSPI Flash under Linux for S32CC platforms
IMAGE_INSTALL:append:s32cc = " mtd-utils "

# Support for accessing MDIO bus for GMAC phys
IMAGE_INSTALL:append:s32cc = " mdio-proxy "

# Tool for flashing the AQR107 firmware using mdio-proxy
IMAGE_INSTALL:append:s32cc = " aquantia-firmware-utility "

# Supporting complex evaluation scenarios
IMAGE_INSTALL += "openssl-misc"
IMAGE_INSTALL:append:s32 = " openssl openssl-dev libcrypto libssl openssl-conf openssl-engines openssl-bin"
IMAGE_INSTALL:remove:s32 = "ipsec-tools"

# Increase the freespace
IMAGE_ROOTFS_EXTRA_SPACE ?= "54000"

# Enable LXC features.
# On LS2 enable it by default. On s32, only by DISTRO_FEATURE
LXC_INSTALL_PACKAGES = "lxc debootstrap"
IMAGE_INSTALL:append:s32 = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' ${LXC_INSTALL_PACKAGES}', '', d)}"
IMAGE_INSTALL:append:ls2 = " ${LXC_INSTALL_PACKAGES}"

# SFTP server
IMAGE_INSTALL:append = " openssh openssh-sftp openssh-sftp-server "

# Other useful tools
IMAGE_INSTALL:append = " rsync irqbalance i2c-tools linuxptp"

# sysfs gpio interface is deprecated, include gpiod tools, lib and headers
IMAGE_INSTALL:append = " libgpiod libgpiod-tools libgpiod-dev"

# add demo sample applications
IMAGE_INSTALL:append = " demo-samples"

# add cpufrequtils package 
IMAGE_INSTALL:append = " cpufrequtils"
