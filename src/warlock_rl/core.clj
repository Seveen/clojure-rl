(ns warlock-rl.core
  (:require [warlock-rl.view.views :as v]
            [zircon.core :as z]))

(z/build-ui {:app     :swing
             :size    [80 50]
             :tileset :wanderlust16x16}
            [v/start-view])
