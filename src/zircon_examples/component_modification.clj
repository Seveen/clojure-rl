(ns zircon-examples.component_modification
  (:require [zircon.component :as c :refer [set-field get-field]]
            [zircon.colors :as l]
            [zircon.core :as z])
  (:import (org.hexworks.zircon.api.uievent KeyCode)))

(def app-config
  {:app     :swing
   :size    [30 10]
   :tileset :wanderlust16x16})

(def view
  (let [label (c/->component
                {:type :label
                 :text "Label"
                 :size [10 1]})
        button (c/->component
                 {:type :button
                  :text "Modify"
                  :alignment-around [label :top-right]})
        handler (fn [event _]
                  (println "handler" event))]
    (c/add-handler button
                   :mouse-released
                   (fn [_ _]
                     (set-field :text label
                                (str (get-field :text label)
                                     "x"))))
    {:root  [label button]
     :handler handler
     :theme (l/color-themes :adriftInDreams)}))

(z/build-ui app-config [view])
