# Colibri X

## Compilation Guide

- Download the Colibri X source code
- Fill out storeFile, storePassword, keyAlias, keyPassword in local.properties to access your release.keystore
- Go to https://console.firebase.google.com/, create two android apps with application IDs org.viento.colibrix and org.viento.colibrix.beta, turn on firebase messaging and download google-services.json, which should be copied into TMessagesProj folder.
- Open the project in the Studio (note that it should be opened, NOT imported).
- Fill out values in TMessagesProj/src/main/java/org/viento/colibrix/Extra.java – there’s a link for each of the variables showing where and which data to obtain.
- Generate TMessagesProj/jni/integrity/genuine.h - https://github.com/brevent/genuine

You are ready to compile Colibri X.

## Credits

<ul>
  <li>Telegram: <a href="https://github.com/DrKLO/Telegram/blob/master/LICENSE">GPLv2</a></li>
  <li>Nekogram: <a href="https://gitlab.com/Nekogram/Nekogram/-/blob/master/LICENSE">GPLv2</a></li>
</ul>
