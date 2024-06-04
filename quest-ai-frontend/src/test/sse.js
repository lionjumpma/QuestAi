function sseBuild(chatMsg, conversation) {
    let source = new EventSource(`http://localhost:8080/chat/stream?content=${chatMsg}&&model=GLM3`);
    source.addEventListener("open", function () {
        console.log("connect");
        chatMsg = "";
        console.log("bus-", conversation);

        // 假设 bus 是一个已经定义好的事件触发器
        bus.emit('bus-send', conversation.id);
    });

    source.addEventListener("content", function (e) {
        message = conversation.msgList[conversation.msgList?.length - 1];
        var content = e.data;
        message["speeches"][0] += content;
    });

    source.addEventListener('complete', (event) => {
        source.close();
        message["loading"] = false;
        conversation.loading = false;
        console.log(message);
    });

    source.addEventListener("error", function (e) {
        console.log("error:" + e.data);
        source.close();
    });

    return source; // 返回 source 以便外部可以控制或测试
}
sseBuild("你好", conversation);
