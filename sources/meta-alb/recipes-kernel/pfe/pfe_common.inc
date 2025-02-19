# Copyright 2019-2024 NXP
#
# PFE common recipe for Master/Slave

inherit module

SUMMARY = "Linux driver for the Packet Forwarding Engine hardware"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE-GPL2.txt;md5=5dcdfe25f21119aa5435eab9d0256af7"

URL ?= "git://github.com/nxp-auto-linux/pfeng;protocol=https;nobranch=1"
SRC_URI = "${URL}"

# Building kernel version specific PFE driver support should be reverted
# once the same version of the PFE driver has been validated for both
# kernel versions (primary and secondary)
KERNEL_MAJ_VER = "${@oe.utils.trim_version("${PREFERRED_VERSION_linux-s32}", 2)}"
SRCREV ?= "${@oe.utils.ifelse(d.getVar('KERNEL_MAJ_VER') == '6.6', 'fdafef25bce0a9676d2915e836219b832ce3dc64', '9ef7ac3ed8d80be28da637d982f336c4239aeb9a')}"

S = "${WORKDIR}/git"
MDIR = "${S}/sw/linux-pfeng"
INSTALL_DIR = "${D}/${base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/net/ethernet/nxp/pfe"

EXTRA_OEMAKE:append =  "${@oe.utils.conditional('SCMI_USE_SCMI_NVMEM', '1', ' PFE_CFG_USE_NVMEM=1', '', d)}"
EXTRA_OEMAKE:append = " KERNELDIR=${STAGING_KERNEL_DIR} MDIR=${MDIR} -C ${MDIR} V=1 drv-build"

COMPATIBLE_MACHINE = "s32g"
