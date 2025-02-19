# Copyright 2023,2024 NXP

DEPENDS += "arm-trusted-firmware-tools-native"

FIP_KEYS_DIR = "${WORKDIR}/keys_fip"
RSA_PRIV_FIP = "${FIP_KEYS_DIR}/fip_rsa2048_private.pem"
RSA_PUB_FIP = "${FIP_KEYS_DIR}/fip_rsa2048_public.pem"
RSA_PUB_FIP_FILE = "fip_rsa2048_public.pem"
INSTALL_PATH_FIP_KEYS = "/etc/keys/secboot/"

do_generate_fip_keys () {
	mkdir -p ${FIP_KEYS_DIR}
	openssl genrsa -out ${RSA_PRIV_FIP} 2048
	openssl rsa -in ${RSA_PRIV_FIP} -pubout -out ${RSA_PUB_FIP}
}

do_generate_fip_keys[depends] += "openssl-native:do_populate_sysroot"
addtask do_generate_fip_keys before do_compile after do_clean_first

do_deploy:append() {
	cp -vf "${RSA_PRIV_FIP}" ${DEPLOYDIR}
	cp -vf "${RSA_PUB_FIP}" ${DEPLOYDIR}
}

do_install:append() {
       install -d ${D}${INSTALL_PATH_FIP_KEYS}
       install -m 0666 ${RSA_PUB_FIP} ${D}${INSTALL_PATH_FIP_KEYS}
}

FILES:${PN}-secboot = "${INSTALL_PATH_FIP_KEYS}${RSA_PUB_FIP_FILE}"
PROVIDES += "${PN}-secboot"
PACKAGES += "${PN}-secboot"
