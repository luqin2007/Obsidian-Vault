一个新的随机数生成器，包含新的随机算法和分布方式。

随机算法
* `linear_congruential`：仅生成整形，低质量，中等速度
* `subtract_with_carry`：可生成整形和浮点，质量中等，速度快
* `mersenne_twister`：仅生成整形，质量高，速度快

标准分布
* `uniform_int_distribution`：离散型均匀分布
* `bernoulli_distribution`：伯努利分布
* `geometric_distribution`：几何分布
* `poisson_distribution`：卜瓦松分布
* `binomial_distribution`：二项分布
* `uniform_real_distribution`：离散型均匀分布
* `exponential_distribution`：指数分布
* `normal_distribution`：正态分布
* `gamma_distribution`：伽马分布

```cpp
#include <iostream>
#include <random>

using namespace std;

int main() {
    uniform_int_distribution<int> distribution(0, 90);
    mt19937 engine;
    for (int i = 0; i < 10; ++i) {
        cout << "Value " << i << ": " << distribution(engine) << endl;
    }
    return 0;
}
```
