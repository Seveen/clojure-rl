(ns zircon.app
  (:require [zircon.tileset :as t])
  (:import (org.hexworks.zircon.api.builder.application AppConfigBuilder)
           (org.hexworks.zircon.api.application CursorStyle)
           (org.hexworks.zircon.api.color TileColor)
           (org.hexworks.zircon.api SwingApplications LibgdxApplications)))

(def cursor-styles
  {:character-foreground `CursorStyle/USE_CHARACTER_FOREGROUND
   :fixed-background `CursorStyle/FIXED_BACKGROUND
   :under-bar `CursorStyle/UNDER_BAR
   :vertical-bar `CursorStyle/VERTICAL_BAR})

(defn- app-config [config]
  (let [[width height] (or (:size config) [80 24])]
    (as-> (.newBuilder AppConfigBuilder/Companion) b
          (.withTitle b (or
                          (:title config)
                          "Zircon Application"))
          (.withSize b width height)
          (.withCursorBlinking b (or
                                   (:cursor-blink config)
                                   false))
          (.withBlinkLengthInMilliSeconds b (or
                                              (:blink-length config)
                                              500))
          (if (:fullscreen config)
            (.fullScreen b)
            b)
          (.withDebugMode b (or
                              (:debug-mode config)
                              false))
          (.withClipboardAvailable b (or
                                       (:clipboard config)
                                       false))
          (.withCursorStyle b (or (cursor-styles (:cursor-style config))
                                  (CursorStyle/FIXED_BACKGROUND)))
          (.withCursorColor b (TileColor/fromString (or (:cursor-color config)
                                                        "#FFFFFF")))
          (.withDefaultTileset b (or (t/cp437-tilesets (:tileset config))
                                     (t/cp437-tilesets :wanderlust16x16)))
          (.enableBetaFeatures b)
          (.build b))))

(defn ->app [config]
  (case (:app config)
    :swing (SwingApplications/startApplication (app-config config))
    :libgdx (LibgdxApplications/startApplication (app-config config))
    (SwingApplications/startApplication (app-config config))))
