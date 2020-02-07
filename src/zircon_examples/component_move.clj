(ns zircon-examples.component-move
  (:require [zircon.component :as c :refer [move-to move-by]]
            [zircon.colors :as l]
            [zircon.core :as z]))

(def app-config
  {:app     :swing
   :size    [60 30]
   :tileset :wanderlust16x16})

(let [panel (c/->component
              {:type        :panel
               :size        [20 10]
               :decorations {:box {}}})
      innerPanel (c/->component
                   {:type        :panel
                    :size        [10 5]
                    :decorations {:box {}}})
      button (c/->component
               {:type     :button
                :text     ["Foo"]
                :position [1 1]})]
  (c/add-child innerPanel button)
  (c/add-child panel innerPanel)
  (z/build-ui app-config [{:root  [panel]
                           :theme (l/color-themes :adriftInDreams)}])
  (Thread/sleep 2000)
  (move-to panel [5 5])
  (Thread/sleep 2000)
  (move-by innerPanel [2 2]))