DESCRIPTION = "Add package silex-wlanbt-fs-firmware for F&S boards"
LICENSE = "CLOSED"

inherit module-base systemd

SRC_URI = "file://silex-wlanbt-fs-firmware-v${PV}.tar.bz2 \
		   file://S02silex \
		   file://silex-mac.service \
		 "
SILEX = "${WORKDIR}/silex-wlanbt-fs-firmware-v${PV}"


SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE:${PN} = "silex-mac.service"


# recipe fus-silex may installed after wpa-supplicant and hostapd are already
# installed. The reason is fus-silex overwrites some binarys of wpa-supplicant
# and hostapd so this recipes must be installed before fus-silex installed.
RDEPENDS:${PN} += "wpa-supplicant hostapd"

do_install() {

# install firmware
	install -d ${D}/run/
	install -d ${D}/lib/firmware/
	install -d ${D}/lib/firmware/wlan
	install -d ${D}/${systemd_unitdir}/system

    cp -r ${SILEX}/firmware/* ${D}/lib/firmware/
	ln -r -s ${D}/lib/firmware/qca/rampatch_tlv_tf_1.1.tlv ${D}/lib/firmware/qca/tfbtfw11.tlv
	ln -r -s ${D}/lib/firmware/qca/nvm_tlv_tf_1.1.bin ${D}/lib/firmware/qca/tfbtnv11.bin

	install -m 0755 ${WORKDIR}/S02silex ${D}/lib/firmware/wlan
	ln -f -s /run/Silex-MAC ${D}/lib/firmware/wlan/wlan_mac.bin
	install -m 0644 ${WORKDIR}/silex-mac.service ${D}/${systemd_unitdir}/system

}

RPROVIDES:${PN} += " wireless-regdb wireless-regdb-static"

FILES:${PN} += "run/"
FILES:${PN} += "lib/firmware/"
FILES:${PN} += "lib/firmware/S02silex"
FILES:${PN} += "${systemd_unitdir}/system/silex-mac.service"

