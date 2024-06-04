// var conv = {
//     "idx": 0,
//     "loading": true,
//     "role": "assistant",
//     "suitable": [0],
//     "msgList": [""]
// }
export interface promptType {
    role: "user" | "assistant" | "system";
    content: string;
}
export interface messageType {
    idx: number;
    loading: boolean;
    role: string;
    timestamp?: number;
    suitable: number[];
    speeches: string[];
}
export interface chatStatus {
    status: "chatting|available"
}
export interface convType {
    id: number;
    title: string;
    timestamp?: number;
    msgList?: messageType[];
    selected?:boolean;
    editable?:boolean;
}
export interface stateType {
    conversationList: convType[]|null;
    conversation?: convType|null;
    chatStatus?: "chatting"|"available";
    chatMsg: string;
    selectedIdx?: number;
}
export interface convListType {
    conversationList: convType[];
}
