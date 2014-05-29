(ns skeletors-big-adventure.core.desktop-launcher
  (:require [skeletors-big-adventure.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. skeletors-big-adventure "skeletors-big-adventure" 1526 512 true)
  (Keyboard/enableRepeatEvents true))
