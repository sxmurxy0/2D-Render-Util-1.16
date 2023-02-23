# 2D-Render-Util-1.16

With this code, you can render different shapes and effects on the screen in your minecraft projects. </br>
Don't forget to copy `assets`.

## Showcase:
![2023-02-23_10 49 44](https://user-images.githubusercontent.com/46312126/220799740-86cffc58-4a96-444d-8d92-8b1e7b79919b.png)
![2023-02-23_10 49 57](https://user-images.githubusercontent.com/46312126/220799759-1802ee26-0659-4122-aabc-24b3a84e9e40.png)

## Bloom and blur guide
To begin with, it is worth noting that `blur` and `bloom` effects are quite resource-intensive things, therefore, it was possible to combine the application of the effect to several objects using the ```registerRenderCall()``` method for both. Once you have specified all the objects, you need to call the ```blur()/bloom()``` method and specify the radius to apply the effect.</br></br>
- In the case of `bloom`, everything is simple - you register render objects with any colors to which the effect should be applied.
- In the case of `blur`, in the form of objects you specify a stencil, in other words, you limit the area to which the blur will be applied.

<sub>I copied this text from the translator, sorry.</sub>
