(ns warlock-rl.view.views
  (:require [zircon.component :as c]
            [zircon.colors :as l]
            [warlock-rl.view.lens :as lens]
            [warlock-rl.system :as s]
            [warlock-rl.ui :as ui]
            [zircon.interop :as i])
  (:import (org.hexworks.zircon.api.uievent KeyCode)))

(def start-view
  (let [game-area (c/->component
                    {:type      :game-component
                     :size      [60 40]
                     :position  [0 0]
                     :game-area [[60 40]
                                 lens/see-world
                                 s/state]})
        log (c/->component
              {:type        :panel
               :size        [60 10]
               :position    [0 40]
               :decorations {:box {:title "Log"}}})
        stats (c/->component
                {:type        :panel
                 :size        [20 50]
                 :position    [60 0]
                 :decorations {:box {:title "Stats"}}})
        labelx (c/->component
                 {:type     :label
                  :size [5 1]
                  :position [0 0]
                  :text     "X"})
        labely (c/->component
                 {:type    :label
                  :size [5 1]
                  :position [0 1]
                  :text     "Y"})]

    (c/add-children stats labelx labely)
    (c/add-handler game-area :mouse-moved (fn [event _]
                                            (let [[x y] (i/pos->vec (.component3 event))]
                                              (c/set-field :text labelx (str x))
                                              (c/set-field :text labely (str y)))))

    {:root    [game-area log stats]
     :handler (fn [event _]
                (ui/update-ui
                  (condp = (.getCode event)
                    KeyCode/KEY_T :right
                    KeyCode/KEY_R :left
                    KeyCode/KEY_F :up
                    KeyCode/KEY_S :down
                    KeyCode/KEY_C :targeting
                    :none)))
     :theme   (l/color-themes :tron)}))
