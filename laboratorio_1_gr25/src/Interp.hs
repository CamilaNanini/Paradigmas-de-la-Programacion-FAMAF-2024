module Interp
  ( interp,
    initial,
  )
where

import Dibujo
import FloatingPic
import Graphics.Gloss (Display (InWindow), color, display, makeColorI, pictures, translate, white, Picture)
import qualified Graphics.Gloss.Data.Point.Arithmetic as V

-- Dada una computación que construye una configuración, mostramos por
-- pantalla la figura de la misma de acuerdo a la interpretación para
-- las figuras básicas. Permitimos una computación para poder leer
-- archivos, tomar argumentos, etc.
initial :: Conf -> Float -> IO ()
initial (Conf n dib intBas) size = display win white $ withGrid fig size
  where
    win = InWindow n (ceiling size, ceiling size) (0, 0)
    fig = interp intBas dib (0, 0) (size, 0) (0, size)
    desp = -(size / 2)
    withGrid p x = translate desp desp $ pictures [p, color grey $ grid (ceiling $ size / 10) (0, 0) x 10]
    grey = makeColorI 100 100 100 100

-- Interpretación de (^^^)
ov :: Picture -> Picture -> Picture -- ov de overlap
ov p q = pictures [p, q] -- Funcion de gloss para superponer todas las pictures de un array

r45 :: FloatingPic -> FloatingPic
r45 fpic d w h = fpic x y z -- f(d+(w+h)/2, (w+h)/2, (h-w)/2)
  where
    x = d V.+ half (w V.+ h) -- Hay que checkear la precedencia que interprete yo
    y = half (w V.+ h)
    z = half (h V.- w)

rot :: FloatingPic -> FloatingPic
rot  fpic d w h = fpic x y z -- f(d+w, h, -w)
  where
    x = d V.+ w
    y = h -- Cualquiera diria que se puede poner h arriba nomas, pero la simetria dice que no
    z = zero V.- w

esp :: FloatingPic -> FloatingPic
esp fpic d w h = fpic x y z --f(d+w, -w, h)
  where
    x = d V.+ w
    y = zero V.- w
    z = h

sup :: FloatingPic -> FloatingPic -> FloatingPic -- Necesito que me checkeen aca
sup fpic gpic x y z = pictures [fpic x y z, gpic x y z]

jun :: Float -> Float -> FloatingPic -> FloatingPic -> FloatingPic
jun m n fpic gpic d w h = ov (fpic d w' h) (gpic (d V.+ w') (r' V.* w) h) -- f(x, w', h) ∪ g(d+w', r'*w, h) con r'=n/(m+n), r=m/(m+n), w'=r*w
  where
    r = m/(m + n)
    w' = r V.* w
    r' = n/(m + n)

api :: Float -> Float -> FloatingPic -> FloatingPic -> FloatingPic
api m n fpic gpic d w h = ov (fpic (d V.+ h') w (r V.* h)) (gpic d w h')
  where
    r = m/(m + n)
    h' = r' V.* h
    r' = n/(m + n)
    
interp :: Output a -> Output (Dibujo a)
interp b = foldDib b r45 rot esp api jun sup