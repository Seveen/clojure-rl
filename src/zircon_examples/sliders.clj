(ns zircon-examples.sliders
  (:require [zircon.component :as c]
            [zircon.core :as z]
            [zircon.colors :as l]
            [zircon.interop :as i]))

(def app-config
  {:app :swing
   :size [60 30]
   :tileset :wanderlust16x16})

(let [panel (c/->component
              {:type        :panel
               :size        [30 28]
               :decorations {:box    {:title "Slider on panel"}
                             :shadow {}}})
      slider1 (c/->component
                {:type        :h-slider
                 :min-value   7
                 :max-value   100
                 :steps       10
                 :decorations {:box {}}
                 :position    [0 5]})
      label (c/->component
              {:type :label
               :size [5 1]
               :text "30"
               :alignment-around [slider1, :right-center]})]
  (c/add-children panel slider1)
  (c/add-children panel label)

  ;Todo cleanup this mess
  (.updateFrom (.getTextProperty label)
               (.getCurrentValueProperty slider1)
               true
               (i/fn->fn1 #(.toString %)))

  (z/build-ui app-config [{:root  [panel]
                           :theme (l/color-themes :amigaOs)}]))
