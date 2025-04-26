# Point Light Attenuation

A point light's attenuation defines how bright it is with respect to its distance from objects. You can set a light's attenuation by using Ogre's **Light::setAttenuation** function. The function takes four Real parameters: *Range*, *Constant*, *Linear*, and *Quadratic*.

The *Range* parameter has nothing to do with the brightness. If an objects distance is greater than the *range*, the light has no effect on the object. If the object is in range, the light's effect is calculated with the following formulas:

* Luminosity = 1 / Attenuation
* Attenuation = *Constant* + *Linear* * Distance + *Quadratic* * Distance^2

Picking the values by trial and error is time consuming and unintuitive. I have calculated some values for the parameters that work well for specified ranges. Using these numbers, the light has 100% intensity at 0 distance, and trails off to near black at a distance equal to the Range. Keep in mind that most of the light falls in the first 20% of the range.

> ***Range Constant Linear Quadratic***
>
> 3250, 1.0, 0.0014, 0.000007
>
> 600, 1.0, 0.007, 0.0002
>
> 325, 1.0, 0.014, 0.0007
>
> 200, 1.0, 0.022, 0.0019
>
> 160, 1.0, 0.027, 0.0028
>
> 100, 1.0, 0.045, 0.0075
>
> 65, 1.0, 0.07, 0.017
>
> 50, 1.0, 0.09, 0.032
>
> 32, 1.0, 0.14, 0.07
>
> 20, 1.0, 0.22, 0.20
>
> 13, 1.0, 0.35, 0.44
>
> 7, 1.0, 0.7, 1.8

|距离|常数项|一次项|二次项|
| ------| --------| --------| ----------|
|7|1.0|0.7|1.8|
|13|1.0|0.35|0.44|
|20|1.0|0.22|0.20|
|32|1.0|0.14|0.07|
|50|1.0|0.09|0.032|
|65|1.0|0.07|0.017|
|100|1.0|0.045|0.0075|
|160|1.0|0.027|0.0028|
|200|1.0|0.022|0.0019|
|325|1.0|0.014|0.0007|
|600|1.0|0.007|0.0002|
|3250|1.0|0.0014|0.000007|

![Attenuation_Graph.jpg](https://wiki.ogre3d.org/img/wiki_up/Attenuation_Graph.jpg)

For example, if you wish to have a light that gradually dims until it has no effect at distance 100:

```c++
Light *light;                               //create pointer to light object
light = mSceneMgr->createLight("Light #1"); //set the pointer to a newly created light
light->setType(Light::LT_POINT);            // make this light a point light
light->setDiffuseColour(1.0, .5, 0.0);      //color the light orange 
light->setSpecularColour(1.0, 1.0, 0.0);    //yellow highlights
light->setAttenuation(100, 1.0, 0.045, 0.0075);
```

Once you have chosen a *Range* and corresponding values, you can tweak the *Constant* and *Linear* values.

Decreasing the *Constant* parameter toward 0.0 brightens the light, while increasing it dims the light.

Increasing the *Linear* parameter causes the light to fade more quickly with distance. I don't suggest changing the *Quadratic* value or decreasing the *Linear* values, as doing so will require you to recalculate the *Range*. Negative numbers are not allowed.

Finally, Decreasing the *Range* value may give your application a performance boost, but decreasing it too much will cause the object to noticeably change brightness when it moves out of range of the point light.

## See also

* [Light](https://wiki.ogre3d.org/-Light "-Light")
* [Light Attenuation Shortcut](https://wiki.ogre3d.org/Light+Attenuation+Shortcut "Light Attenuation Shortcut") - code snippet

# 参考

```cardlink
url: https://wiki.ogre3d.org/tiki-index.php?page=-Point+Light+Attenuation
title: "-Point Light Attenuation | Ogre Wiki"
description: "Ogre Wiki"
host: wiki.ogre3d.org
```

