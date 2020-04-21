(ns warlock-rl.core
  (:require [warlock-rl.view.start-view]
            [warlock-rl.view.play-view]
            [zircon.core :as z]
            [warlock-rl.ui :as ui]))

(ui/init-ui-state
  (z/build-ui {:app        :swing
               :size       [80 50]
               :tileset    :wanderlust16x16
               :close-behavior :no-close
               :start-with :start-view}
              [warlock-rl.view.start-view/start-view
               warlock-rl.view.play-view/play-view]))