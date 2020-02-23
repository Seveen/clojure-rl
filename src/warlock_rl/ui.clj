(ns warlock-rl.ui
  (:require [com.rpl.specter :refer [select select-first transform setval]]
            [warlock-rl.system :refer [update-world]]))

(def ui-state (atom {}))

(defn init-ui-state [app-map]
  (reset! ui-state
          (merge
            app-map
            {:commands       []
             :stack          []
             :mouse-position [0 0]
             :viewport       {:position [0 0]
                              :size     [60 40]}})))

(defn peek-ui-state [state]
  (peek (select-first [:stack] state)))

(defn pop-ui-state [state]
  (transform [:stack] #(when (seq %) (pop %)) state))

(defn push-ui-state [state new-state]
  (transform [:stack] #(conj % new-state) state))

(defn get-view [id]
  (select-first [:view id] @ui-state))

(defn update-mouse-position [position]
  (swap! ui-state
         (fn [state position]
           (setval [:mouse-position] position state))
         position))

(defn get-mouse-position []
  (select-first [:mouse-position] @ui-state))

(defn switch-view [state]
  (println "Coin")
  state)

(def ui-command
  #{:targeting
    :cancel
    :click})

(defn update-ui [command]
  (if (contains? ui-command command)
    (swap! ui-state
           (fn [state command]
             (case command
               :targeting (push-ui-state state :targeting)
               :cancel (pop-ui-state state)
               :click state
               state))
           command)
    (update-world command)))