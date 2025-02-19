FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append = " \
	file://fix-authorized-principals-command.patch \
	file://CVE-2023-48795.patch \
"
