(ns zircon.view
  (:import
    (org.hexworks.zircon.api.uievent KeyboardEventType)
    (org.hexworks.zircon.api.view.base BaseView)
    (java.util.function BiConsumer)
    (org.hexworks.zircon.api Functions)))

(defn handler [callback]
  (reify BiConsumer
    (accept [_ t u]
      (callback t u))))

(defn make-view
  [app config]
    (proxy [BaseView] [(.getTileGrid app) (config :theme)]
      (onDock []
        (doall (map #(.addComponent (.getScreen this) %)
                    (config :root)))
        (.handleKeyboardEvents
          (.getScreen this)
          KeyboardEventType/KEY_PRESSED
          (Functions/fromBiConsumer (handler (or (config :handler)
                                                 #())))))))
