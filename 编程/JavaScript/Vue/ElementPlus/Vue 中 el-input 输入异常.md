涉及组件：`element-plus` 的 `<el-form>` 和 `<el-input>`

将选项式 API 的代码转换成组合式 API 后，`<el-form>` 中的 `<el-input>` 出现输入问题

* 焦点内输入无效
* 丢失焦点后输入最后一次输入的字符

![[1717345656219-output-20240603003013-3e0wpag.gif]]

```html
<script>
export default {
  name: 'LoginView',
  data() {
    return {
      loginForm: {},
      // ...
    }
  },
  methods: {
    login(loginForm) {
      this.$refs.loginForm.validate(valid => {
          // do valid and login
        })
    },
    cancel() {
      this.$refs.loginForm.resetFields()
    }
  }
}
</script>

<template>
<el-dialog title="管理员登录">
  <div class="box">
    <el-form ref="loginForm" :model="loginForm" :rules="rules">
      <el-form-item label="用户名" prop="uname">
        <el-input v-model="loginForm.uname" 
                  placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item label="密码" prop="upwd">
        <el-input v-model="loginForm.upwd" 
                  laceholder="请输入密码" show-password />
      </el-form-item>
      <el-form-item>
        <el-button type="danger" 
                   @click="cancel">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</el-dialog>
</template>
```

```html
<script setup>
import {ref, reactive} from "vue"
import {useRoute, useRouter} from "vue-router"

const loginForm = reactive({
})
// ...

const route = useRoute()
const router = useRouter()

const login = loginForm => {
  loginForm.validate(valid => {
    // do validate and login
  })
}

const cancel = () => loginForm.resetFields()
</script>

<template>
<el-dialog title="管理员登录">
  <div class="box">
    <el-form ref="loginForm" :model="loginForm" :rules="rules">
      <el-form-item label="用户名" prop="uname">
        <el-input v-model="loginForm.uname" 
                  placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item label="密码" prop="upwd">
        <el-input v-model="loginForm.upwd" 
                  laceholder="请输入密码" show-password />
      </el-form-item>
      <el-form-item>
        <el-button type="danger" 
                   @click="cancel">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</el-dialog>
</template>
```

修复：

将 `ref` 与 `:model` 绑定到不同对象中，响应式可以使用 `Vue.ref` 创建 `ref` 变量

```html
<script setup>
import {ref, reactive} from "vue"
const loginFormRef = ref()
const loginForm = reactive({})
loginForm.value.validate(value => ...)
</script>

<template>
<el-dialog title="管理员登录">
  <div class="box">
    <el-form ref="loginFormRef" :model="loginForm" :rules="rules">
      <el-form-item label="用户名" prop="uname">
        <el-input v-model="loginForm.uname" placeholder="请输入用户名" />
      </el-form-item>
    </el-form>
  </div>
</el-dialog>
</template>
```

‍
