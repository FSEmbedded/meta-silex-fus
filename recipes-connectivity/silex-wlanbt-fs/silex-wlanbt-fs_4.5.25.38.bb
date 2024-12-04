DESCRIPTION = "Add package silex-wlan-fs for F&S boards"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://CORE/HDD/src/wlan_hdd_main.c;beginline=1;endline=27;md5=9efdd522865bb2e5aeec57e2869ed396"

inherit module

kfirmdir = "/lib/firmware"
kmoddir = "/lib/modules/${KERNEL_VERSION}"

SRCBRANCH = "caf-wlan/CNSS.LEA.NRT_3.1"

SRC_URI = "git://git.codelinaro.org/clo/la/platform/vendor/qcom-opensource/wlan/qcacld-2.0;protocol=https;branch=${SRCBRANCH}"
SRCREV = "c44ddbe33039fc6fb12042832611c48e1838da2d"

SRC_URI += "  \
			file://0001-qcacld-2.0-fix-SAP-fail-to-startup-after-channel-swi.patch \
			file://0002-qcacld-2.0-Wake-lock-API-changes-to-support-various-.patch \
			file://0003-qcacld-2.0-Replace-get_monotonic_boottime-with-ktime.patch \
			file://0004-qcacld-2.0-ndo_select_queue-support-for-5.4-kernel.patch \
			file://0005-qcacld-2.0-Refine-vos_get_bootbased_boottime_ns-basi.patch \
			file://0006-qcacld-2.0-Define-vendor_command_policy-macro-to-sup.patch \
			file://0007-qcacld-2.0-Fix-compilation-error-to-support-kernel-v.patch \
			file://0008-qcacld-2.0-iapp_work.deferred_work-used-but-not-init.patch \
			file://0009-qcacld-2.0-Fix-sdio-mbox-corruption-on-rome-card.patch \
			file://0010-qcacld-2.0-Initialize-preauth-node.patch \
			file://0011-qcacld-2.0-Flush-RX-packets-for-peer-after-key-insta.patch \
			file://0012-qcacld-2.0-Discard-frag-frames-if-the-PN-is-not-cons.patch \
			file://0013-qcacld-2.0-Drop-mcast-and-plaintext-frags-in-protect.patch \
			file://0014-qcacld-2.0-Add-eapol-sanity-checking-for-intra-bss-f.patch \
			file://0015-qcacld-2.0-Add-eapol-checking-for-intra-bss-fwd-indi.patch \
			file://0016-Revert-qcacld-2.0-Add-regdb.patch \
			file://0017-qcacld-2.0-Refactor-cld-regdb-feature.patch \
			file://0018-Support-newer-kernel-versions-avoid-compiler-warning.patch \
			file://0019-Fix-building-error-for-Kernel-5.15.150.patch \
			file://0020-Fix-stdarg.h-inclusion-error.patch \
"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += " \
		WLAN_ROOT=${S} \
		MODNAME=wlan \
		CONFIG_LINUX_QCMBR=y  \
		CONFIG_NON_QC_PLATFORM=y  \
		CONFIG_WLAN_THERMAL_SHUTDOWN=0 \
		CONFIG_CLD_HL_SDIO_CORE=y  \
		CONFIG_PER_VDEV_TX_DESC_POOL=1 \
		SAP_AUTH_OFFLOAD=1 \
		CONFIG_QCA_LL_TX_FLOW_CT=1 \
		CONFIG_WLAN_FEATURE_FILS=y  \
		CONFIG_FEATURE_COEX_PTA_CONFIG_ENABLE=y  \
		CONFIG_QCA_SUPPORT_TXRX_DRIVER_TCP_DEL_ACK=y  \
		CONFIG_WLAN_WAPI_MODE_11AC_DISABLE=y  \
		CONFIG_WLAN_WOW_PULSE=y  \
		CONFIG_WLAN_FEATURE_11W=y  \
		CONFIG_TXRX_PERF=y  \
		CONFIG_PMF_SUPPORT=y  \
		CONFIG_QCA_CLD_WLAN=m \
		CONFIG_QCA_WIFI_ISOC=0 \
		CONFIG_QCA_WIFI_2_0=1 \
		WLAN_OPEN_SOURCE=1 \
"

# recipe fus-silex may installed after wpa-supplicant and hostapd are already
# installed. The reason is fus-silex overwrites some binarys of wpa-supplicant
# and hostapd so this recipes must be installed before fus-silex installed.
RDEPENDS:${PN} += "wpa-supplicant hostapd"

do_install() {
    install -d ${D}${kmoddir}/extra
    install -m 0644 ${S}/wlan.ko ${D}${kmoddir}/extra
}

FILES:${PN} += "${kmoddir}/extra-/wlan.ko"
