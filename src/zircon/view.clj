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

(defn make-view [app config]
  [(config :id) (proxy [BaseView] [(.getTileGrid app) (config :theme)]
     (onDock []
       (doall (map #(.addComponent (.getScreen this) %)
                   (config :root)))
       (when (config :on-dock)
         ((config :on-dock)))
       (when (config :handler)
         (.processKeyboardEvents
           (.getTileGrid app)
           KeyboardEventType/KEY_PRESSED
           (Functions/fromBiConsumer (handler (config :handler))))))
     (onUndock []
       (when (config :on-undock)
         (config :on-undock))))])

(defn swap-views [view other]
  (.replaceWith view other))