const path = require('path')

module.exports = {
    mode: 'development',
    // 入口
    entry: {
        myindex: './src/myindex.js',
        mymain: './src/mymain.js'
    },
    output: {
        path: path.resolve(__dirname, 'dist'),
        publicPath: './dist/',
        // 输出名称
        filename: '[name]-bundle.js'
    },
    // webpack-dev-server 的配置
    devServer: {
        static: '../multi-entries'
    }
}