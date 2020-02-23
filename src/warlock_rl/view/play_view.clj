(ns warlock-rl.view.play-view
  (:require [zircon.component :as c]
            [zircon.colors :as l]
            [warlock-rl.view.lens :as lens]
            [warlock-rl.view.tiles :as t]
            [warlock-rl.system :as s]
            [warlock-rl.ui :as ui]
            [zircon.interop :as i])
  (:import (org.hexworks.zircon.api.uievent KeyCode)))

(def play-view
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
                 :size        [20 40]
                 :position    [60 0]
                 :decorations {:box {:title "Stats"}}})
        info-panel (c/->component
                     {:type        :panel
                      :size        [20 10]
                      :position    [60 40]
                      :decorations {:box {:title "UI Infos"}}})
        labelx (c/->component
                 {:type     :label
                  :size     [5 1]
                  :position [0 0]
                  :text     "X"})
        labely (c/->component
                 {:type     :label
                  :size     [5 1]
                  :position [0 1]
                  :text     "Y"})
        labelstate (c/->component
                     {:type     :label
                      :size     [10 1]
                      :position [0 2]
                      :text     "State"})
        mouse-icon (c/->component
                     {:type     :icon
                      :position [0 0]
                      :icon     ((t/tiles :transparent-cursor))})]

    (c/add-children info-panel labelx labely labelstate)
    (c/add-children game-area mouse-icon)

    (c/add-handler game-area :mouse-moved
                   (fn [event _]
                     (let [[x y] (i/pos->vec (.getPosition event))]
                       (c/set-field labelx :text (str x))
                       (c/set-field labely :text (str y))
                       (ui/update-mouse-position [x y]))))
    (c/add-handler game-area :mouse-pressed
                   (fn [_ _]
                     (ui/update-ui :click)))

    (c/set-field mouse-icon :hidden true)

    (add-watch ui/ui-state :watch-ui-state
               (fn [key atom old-state new-state]
                 (case (ui/peek-ui-state new-state)
                   :targeting (do (c/set-field labelstate :text "Targeting")
                                  (c/set-field mouse-icon :hidden false)
                                  (c/move-to mouse-icon (ui/get-mouse-position)))
                   (do (c/set-field labelstate :text "Nop")
                       (c/set-field mouse-icon :hidden true)))))

    {:id      :play-view
     :root    [game-area log stats info-panel]
     :handler (fn [event _]
                (ui/update-ui
                  (condp = (.getCode event)
                    KeyCode/KEY_T :right
                    KeyCode/KEY_R :left
                    KeyCode/KEY_F :up
                    KeyCode/KEY_S :down
                    KeyCode/ESCAPE :cancel
                    KeyCode/KEY_C :targeting
                    :none)))
     :theme   (l/color-themes :tron)}))
