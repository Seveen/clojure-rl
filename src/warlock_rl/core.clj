(ns warlock-rl.core
  (:require [warlock-rl.view.views :as v]
            [zircon.core :as z]))

(.dock (first (:view (z/build-ui {:app     :libgdx
                                  :size    [80 50]
                                  :tileset :wanderlust16x16}
                                 [v/start-view]))))
