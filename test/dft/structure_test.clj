(ns dft.structure-test
  (:require [clojure.test :refer :all]
            [dft.structure :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as str]))


(deftest write-xyz-test
  (let [coords [[5      [ 1.424797997      -0.847852878      -0.002228727]]
                [5      [-1.447210319       0.859412671       0.004733071]]
                [1      [-2.382522328       1.441193845      -2.019397054]]
                [1      [-0.994442629      -1.653775077      -0.007887102]]
                [1      [-2.397254682       1.410908778       2.032323129]]
                [1      [ 0.974857648       1.662992362       0.024631332]]
                [1      [ 2.370642021      -1.432911205       2.017494176]]
                [1      [ 2.374867596      -1.396487633      -2.030111907]]]
        xyz (str/split-lines (write-xyz coords "Diborane structure"))]
    (testing "Should produce correct raw-xyz representation."
      (is (= (count xyz) 10)
          "Number of lines = 2 + number of atoms.")

      (is (= (first xyz) "8")
          "Number of atoms in the first line.")

      (is (= (second xyz) "Diborane structure")
          "Commentary in the second line.")

      (is (= (str/trim (nth xyz 3))
             "B    -1.447210319000      +0.859412671000      +0.004733071000")
          "Boron in the fourth line.")

      (is (= (str/trim (nth xyz 5))
             "H    -0.994442629000      -1.653775077000      -0.007887102000")
          "Hydrogen in the sixth line."))))


(deftest read-xyz-test
  (let [xyz (slurp (io/resource "test/diborane.xyz"))
        coords (read-xyz xyz)]
    (testing "Should read coordinates' array from `.xyz`."
      (is (= (count coords) 8)
          "Only atoms in array. Diborane has 8.")

      (is (= (first coords)
             [5      [ 1.424797997      -0.847852878      -0.002228727]])
          "First atom record (Boron)")

      (is (= (last coords)
             [1      [ 2.374867596      -1.396487633      -2.030111907]])
          "Last atom record (Hydrogen)"))))
