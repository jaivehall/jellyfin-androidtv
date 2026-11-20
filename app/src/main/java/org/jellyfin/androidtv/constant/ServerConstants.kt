package org.jellyfin.androidtv.constant

import org.jellyfin.androidtv.BuildConfig

object ServerConstants {
	const val APP_INFO_NAME = "Finplayer for Android TV"
	/**
	 * Default Jellyfin server address. Injected at build time via BuildConfig so
	 * the value is never stored in version-controlled sources. Empty when the
	 * build was not configured with a default server address - callers must treat
	 * a blank value as "no default server" and fall back to the server setup UI.
	 */
	val DEFAULT_SERVER_ADDRESS: String = BuildConfig.DEFAULT_SERVER_ADDRESS

	/**
	 * When true, the default server address is submitted automatically on the
	 * "add server" screen (no user confirmation). When false, it is only
	 * pre-filled into the input field and the user taps Connect.
	 */
	const val AUTO_CONNECT_DEFAULT_SERVER = false
}
