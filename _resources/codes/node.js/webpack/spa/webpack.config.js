const path = require('path');
module.exports = {
    mode: 'development',
    // 入口
    entry: './src/main.js',
    // 输出到 /dist/bundle.js
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'bundle.js',
        publicPath: './dist/'
    },
    devServer: {
        // 当前项目目录名
        static: '../spa'
    }
}