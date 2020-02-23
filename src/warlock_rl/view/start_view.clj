(ns warlock-rl.view.start-view
  (:require [zircon.component :as c]
            [zircon.colors :as l]
            [zircon.view :as v]
            [warlock-rl.ui :as ui]))

(def start-view
  (let [panel (c/->component
                {:type        :panel
                 :size        [80 50]
                 :decorations {:box {}}})
        title (c/->component
                {:type             :label
                 :text             "Some Clojure RL"
                 :alignment-within [panel :center]})
        start-button (c/->component
                       {:type             :button
                        :text             "Start New Game"
                        :alignment-around [title :bottom-center]})
        load-button (c/->component
                      {:type             :button
                       :alignment-around [start-button :bottom-center]})]
    (c/add-handler start-button :mouse-pressed
                   (fn [_ _]
                     (v/swap-views (ui/get-view :start-view)
                                   (ui/get-view :play-view))))

    (c/add-children panel title start-button load-button)

    {:id    :start-view
     :root  [panel]
     :theme (l/color-themes :tron)}))
