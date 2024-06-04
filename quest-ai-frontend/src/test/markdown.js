import {marked} from "marked";



function countAndConcat(str, substr) {
    // 使用正则表达式的全局匹配来查找子字符串
    const matches = str.match(new RegExp(substr, 'g'));

    // 判断子字符串的个数是奇数还是偶数
    const count = matches ? matches.length : 0;
    const isOdd = count % 2 === 1;

    // 根据判断结果返回相应的字符串
    return isOdd ? str + "\n" + substr : str;
}
function mdToHtml(md, conv) {
    if (md == "") {
        return "<p></p>"
    }

    md = countAndConcat(md, "```")
    // 处理Markdown文本中标题和列表项后缺少空格的问题
    md = md.replace(/(^|\n)(#+)([^\s#])/g, "$1$2 $3")  // 处理标题
        .replace(/(^|\n)(-)([^\s-])/g, "$1$2 $3");  // 处理无序列表项

    var htmlMD = marked.parse(md);
    htmlMD = htmlMD.trim();
    console.log(htmlMD)
    return htmlMD;
}
mdToHtml("###第一阶段：Java基础\n" +
    "\n" +
    "**1.Java编程入门**\n" +
    "-理解程序的基本概念\n" +
    "-配置Java开发环境\n" +
    "-Java编程过程和Java的特点\n" +
    "-程序的运行机制", countAndConcat)
