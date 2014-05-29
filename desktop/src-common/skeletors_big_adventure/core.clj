(ns skeletors-big-adventure.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.math :refer :all]))

(declare skeletors-big-adventure main-screen)
(def speed 15)

(defn- get-direction []
  (cond
   (is-pressed? :dpad-left) :left
   (is-pressed? :dpad-right) :right))

(defn- update-player-position [{:keys [player?] :as entity}]
  (if player?
    (let [direction (get-direction)
          new-x (case direction
                  :right (+ (:x entity) speed)
                  :left (- (:x entity) speed))]
      (when (not= (:direction entity) direction)
        (texture! entity :flip true false))
      (assoc entity :x new-x :direction direction))
    entity))

(defn- update-hit-box [{:keys [player? gem?] :as entity}]
  (if (or player? gem?)
    (assoc entity :hit-box (rectangle (:x entity) (:y entity) (:width entity) (:height entity)))
    entity))

(defn- remove-touched-gems [entities]
  (if-let [gems (filter #(contains? % :gem?) entities)]
    (let [player (some #(when (:player? %) %) entities)
          touched-gems (filter #(rectangle! (:hit-box player) :overlaps (:hit-box %)) gems)]
      (remove (set touched-gems) entities))
    entities))

(defn- move-player [entities]
  (->> entities
       (map (fn [entity]
              (->> entity
                   (update-player-position)
                   (update-hit-box))))
       (remove-touched-gems)))

(defn- spawn-gem []
  (let [x (+ 50 (rand-int 1140))
        y (+ 50 (rand-int 30))]
  (assoc (texture "gem.png") :x x :y y :width 50 :height 50 :gem? true)))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage))
    (add-timer! screen :spawn-gem 1 2)
    (let [background (assoc (texture "video-game-background-animated.gif") :width 1526 :height 512)
          player (assoc (texture "pure-skeletor.png") :x 50 :y 50 :width 113 :height 112 :player? true :direction :left)]
      [background player]))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))

  :on-key-down
  (fn [screen entities]
    (cond
     (is-pressed? :r) (app! :post-runnable #(set-screen! skeletors-big-adventure main-screen))
     (get-direction) (move-player entities)
     :else entities))

  :on-timer
  (fn [screen entities]
    (case (:id screen)
      :spawn-gem (conj entities (spawn-gem)))))

(defgame skeletors-big-adventure
  :on-create
  (fn [this]
    (set-screen! this main-screen)))

(-> main-screen :entities deref)
