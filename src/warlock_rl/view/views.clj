(ns warlock-rl.view.views
  (:require [zircon.game-area :as z]
            [zircon.component :as c]
            [zircon.colors    :as l]
            [warlock-rl.system :as s])
  (:import (org.hexworks.zircon.api.uievent KeyCode UIEventResponse)))

(def start-view
 (let [game-area (c/->component
                   {:type      :game-component
                    :size      [60 40]
                    :position  [0 0]
                    :game-area (z/area)})
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
       handler (fn [event _]
                 (z/paint-world
                   (s/update-world
                     (condp = (.getCode event)
                       KeyCode/KEY_T :right
                       KeyCode/KEY_R :left
                       KeyCode/KEY_F :up
                       KeyCode/KEY_S :down
                       KeyCode/KEY_C :use-skill
                       :none))))]
 {:root    [game-area log stats]
  :handler handler
  :theme   (l/color-themes :tron)}))
