(ns dft.structure
  (:require
   [clojure.string :as str]))


(def elements
  "Array of elements symbols from Mendeleev's Periodic Table."
  ["H" "He"
   "Li" "Be" "B" "C" "N" "O" "F" "Ne"
   "Na" "Mg" "Al" "Si" "P" "S" "Cl" "Ar"
   "K" "Ca" "Sc" "Ti" "V" "Cr" "Mn" "Fe" "Co" "Ni" "Cu" "Zn" "Ga" "Ge" "As" "Se" "Br" "Kr"
   "Rb" "Sr" "Y" "Zr" "Nb" "Mo" "Tc" "Ru" "Rh" "Pd" "Ag" "Cd" "In" "Sn" "Sb" "Te" "I" "Xe"
   "Cs" "Ba"
   ;Lanthanides
   "La" "Ce" "Pr" "Nd" "Pm" "Sm" "Eu" "Gd" "Tb" "Dy" "Ho" "Er" "Tm" "Yb" "Lu"
   "Hf" "Ta" "W" "Re" "Os" "Ir" "Pt" "Au" "Hg" "Tl" "Pb" "Bi" "Po" "At" "Rn"
   "Fr" "Ra"
   ;Actinides
   "Ac" "Th" "Pa" "U" "Np" "Pu" "Am" "Cm" "Bk" "Cf" "Es" "Fm" "Md" "No" "Lr"
   "Rf" "Db" "Sg" "Bh" "Hs" "Mt" "Ds" "Rg" "Cn" "Nh" "Fl" "Mc" "Lv" "Ts" "Og"])


(defn atomic-number [sym]
  "Return atomic number of element conventionally labeled by `sym`."
  (let [index (inc (.indexOf elements sym))]
    (if (pos? index) index nil)))


(defn- write-xyz-atomrow [arow]
  "Outputs row of element with pretty vague format style."
  (let [arow (vec (flatten arow))]
    (apply #(format "%-4s %+-20.12f %+-20.12f %+-20.12f %n" %1 %2 %3 %4)
           (assoc arow 0 (nth elements (dec (first arow)))))))


(defn write-xyz [coords-array & [commentary]]
  "Write array of coordinates in a `.xyz`-file format string."
  (apply str
         (concat (list (format "%d%n" (count coords-array))
                       (format "%s%n" (or commentary "")))
                 (mapv write-xyz-atomrow  coords-array))))


(defn- read-xyz-atomrow [srow]
  "Reads row of element from string that is similar to `.xyz`."
  (let [datarow (str/split (str/trim srow) #"\s+")]
    (vec (list (atomic-number (first datarow))
               (mapv #(Double. %) (rest datarow))))))


(defn read-xyz [data-xyz]
  "Read coordinates array from a `.xyz`-file format string."
  (let [data (str/split-lines data-xyz)
        num-of-atoms (Integer. (first data))
        commentary (second data)]
    (mapv read-xyz-atomrow
          (->> data (take (+ num-of-atoms 2)) (drop 2)))))
