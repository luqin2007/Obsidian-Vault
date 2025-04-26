const path = require('path')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')

module.exports = {
    mode: 'development',
    entry: './src/main.js',
    output: {
        path: path.join(__dirname, 'dist'),
        publicPath: './dist/',
        filename: 'bundle.js'
    },
    module: {
        rules: [{
            test: /\.css$/,
            use: [ MiniCssExtractPlugin.loader, 'css-loader' ],
        }]
    },
    plugins: [
        new MiniCssExtractPlugin({ filename: 'common.css' })
    ]
}