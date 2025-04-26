涉及组件：`element-plus` 的 `<el-form>`

将选项式 API 的代码转换成组合式 API 后，`<el-form>` 绑定的对象无相关方法

![[20240603_81-20240603004705-far0cq2.gif]]

```html
<script>
export default {
  name: 'LoginView',
  data() {
    return {
      loginForm: {},
  },
  methods: {
    login(loginForm) {
      this.$refs.loginForm.validate(valid => {
        // valid and login
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
      <!-- ... -->
      <el-form-item>
        <el-button type="primary" 
                   @click="login(loginForm)">登录</el-button>
        <el-button type="danger" 
                   @click="cancel">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</el-dialog>
</template>
```

```html
<script>
import {ref, reactive} from "vue"
import {ElMessageBox} from 'element-plus'

const loginFormRef = ref()
const loginForm = reactive({})

const login = loginForm => {
  loginForm.validate(valid => {
    // valid and login
  })
}

const cancel = () => loginForm.resetFields()
</script>

<template>
<el-dialog title="管理员登录">
  <div class="box">
    <el-form ref="loginFormRef" :model="loginForm" :rules="rules">
      <!-- ... -->
      <el-form-item>
        <el-button type="primary" 
                   @click="login(loginForm)">登录</el-button>
        <el-button type="danger" 
                   @click="cancel">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</el-dialog>
</template>
```

解决方案：`:model` 绑定的对象并不是组件对象，应使用 `ref` 属性指向的对象

```js
import {ref, reactive} from "vue"
import {ElMessageBox} from 'element-plus'

const loginFormRef = ref()
const loginForm = reactive({})

const login = loginForm => {
  loginFormRef.value.validate(valid => {
    // valid and login
  })
}

const cancel = () => loginFormRef.value.resetFields()
```

‍
