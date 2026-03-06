export const id = 787;
export const ids = [787];
export const modules = {

/***/ 48787:
/***/ ((__webpack_module__, __webpack_exports__, __webpack_require__) => {

__webpack_require__.a(__webpack_module__, async (__webpack_handle_async_dependencies__, __webpack_async_result__) => { try {
__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   handler: () => (/* binding */ handler)
/* harmony export */ });
/* harmony import */ var _lib_simple_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(6365);
/* harmony import */ var util__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(39023);
/* harmony import */ var ws__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(96641);
/* harmony import */ var url__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(87016);
/* harmony import */ var path__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(16928);
/* harmony import */ var fs__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(79896);
/* harmony import */ var chalk__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(18116);
var __webpack_async_dependencies__ = __webpack_handle_async_dependencies__([_lib_simple_js__WEBPACK_IMPORTED_MODULE_0__]);
_lib_simple_js__WEBPACK_IMPORTED_MODULE_0__ = (__webpack_async_dependencies__.then ? (await __webpack_async_dependencies__)() : __webpack_async_dependencies__)[0];
/**
 * ╔═╗ ╔═╗ ╔╦╗ ╦ ╔═╗ ╔═╗      ╔╗  ╔═╗ ╔╦╗
 * ║ ╦ ║ ║  ║  ║ ║   ╠═╣      ╠╩╗ ║ ║  ║ 
 * ╚═╝ ╚═╝  ╩  ╩ ╚═╝ ╩ ╩      ╚═╝ ╚═╝  ╩ 
 * @author Leandro Rocha
 * @link https://github.com/leandromemes
 * @project Gotica Bot
 */










const { proto } = (await Promise.resolve(/* import() */).then(__webpack_require__.t.bind(__webpack_require__, 90466, 19))).default
const isNumber = x => typeof x === 'number' && !isNaN(x)

async function handler(chatUpdate) {
    this.msgqueque = this.msgqueque || []
    this.uptime = this.uptime || Date.now()
    if (!chatUpdate) return
    this.pushMessage(chatUpdate.messages).catch(console.error)
    let m = chatUpdate.messages[chatUpdate.messages.length - 1]
    if (!m) return;

    if (global.db.data == null) await global.loadDatabase()
    let sender;
    try {
        let messageStubType = m.messageStubType
        let messageStubParameters = m.messageStubParameters

        let idLista = null;
        try {
            const rawMsg = m.message || {};
            const type = Object.keys(rawMsg)[0];
            const content = rawMsg[type];
            idLista = content?.singleSelectReply?.selectedRowId || 
                      content?.selectedButtonId || 
                      content?.nativeFlowResponseMessage?.paramsJson ||
                      m.message?.templateButtonReplyMessage?.selectedId ||
                      m.message?.interactiveResponseMessage?.nativeFlowResponseMessage?.paramsJson;
            if (idLista && idLista.startsWith('{')) {
                let parsed = JSON.parse(idLista);
                idLista = parsed.id || parsed.selectedId || idLista;
            }
        } catch (e) { }

        m = (0,_lib_simple_js__WEBPACK_IMPORTED_MODULE_0__/* .smsg */ .L)(this, m) || m
        if (!m) return

        if (messageStubType) {
            m.messageStubType = messageStubType
            m.messageStubParameters = messageStubParameters
        }

        if (idLista) {
            let pref = global.prefix || '/'
            m.text = idLista.startsWith(pref) ? idLista : pref + idLista;
        }

        if (m.isGroup) {
            const chat = global.db.data.chats[m.chat];
            const botJid = this.user.jid;
            const isOnCommand = m.text && m.text.match(/^[.#/]on(bot|ativar)?(\s|$)/i);
            if (chat?.bannedBots?.includes(botJid) && !isOnCommand) return;

            if (chat?.primaryBot) {
                const universalWords = ['resetbot', 'resetprimario', 'botreset'];
                const firstWord = m.text ? m.text.trim().split(' ')[0].toLowerCase().replace(/^[./#]/, '') : '';
                if (!universalWords.includes(firstWord)) {
                    if (this?.user?.jid !== chat.primaryBot) return;
                }
            }
        }

        sender = m.isGroup ? (m.key.participant ? m.key.participant : m.sender) : m.key.remoteJid;
        if (!sender && m.messageStubType) sender = m.key.remoteJid

        const groupMetadata = m.isGroup ? { ...(this.chats[m.chat]?.metadata || await this.groupMetadata(m.chat).catch(_ => null) || {}), ...(((this.chats[m.chat]?.metadata || await this.groupMetadata(m.chat).catch(_ => null) || {}).participants) && { participants: ((this.chats[m.chat]?.metadata || await this.groupMetadata(m.chat).catch(_ => null) || {}).participants || []).map(p => ({ ...p, id: p.jid, jid: p.jid, lid: p.lid })) }) } : {}
        const participants = ((m.isGroup ? groupMetadata.participants : []) || []).map(participant => ({ id: participant.jid, jid: participant.jid, lid: participant.lid, admin: participant.admin }))

        if (m.isGroup) {
            if (sender && sender.endsWith('@lid')) {
                const pInfo = participants.find(p => p.lid === sender);
                if (pInfo && pInfo.id) {
                    sender = pInfo.id;
                    if (m.key) m.key.participant = pInfo.id;
                    try { m.sender = pInfo.id } catch (e) {}
                }
            }
        }

        m.exp = 0
        m.coin = false

        try {
            let user = global.db.data.users[sender]
            if (typeof user !== 'object') global.db.data.users[sender] = {}
            if (user) {
                if (!isNumber(user.exp)) user.exp = 0
                if (!isNumber(user.coin)) user.coin = 0 
                if (!isNumber(user.bank)) user.bank = 0
                if (!isNumber(user.level)) user.level = 0
                if (!('registered' in user)) user.registered = false
                if (!('muto' in user)) user.muto = false
            } else global.db.data.users[sender] = { exp: 0, coin: 0, bank: 0, level: 0, registered: false, muto: false, name: m.name }

            let chat = global.db.data.chats[m.chat]
            if (typeof chat !== 'object') global.db.data.chats[m.chat] = {}
            if (chat) {
                if (!('isBanned' in chat)) chat.isBanned = false
                if (!('welcome' in chat)) chat.welcome = true
                if (!('modoreal' in chat)) chat.modoreal = false 
            } else global.db.data.chats[m.chat] = { isBanned: false, welcome: true, modoreal: false }

        } catch (e) { console.error(e) }

        if (opts['nyimak']) return
        if (!m.fromMe && opts['self']) return
        if (typeof m.text !== 'string') m.text = ''

        if (global.db.data.users[sender]?.banned && !m.fromMe) return

        const _user = global.db.data.users[sender]
        const userGroup = (m.isGroup ? participants.find((u) => this.decodeJid(u.jid) === sender) : {}) || {}
        const botGroup = (m.isGroup ? participants.find((u) => this.decodeJid(u.jid) == this.user.jid) : {}) || {}
        const isAdmin = userGroup?.admin == "admin" || userGroup?.admin == "superadmin" || false
        const isBotAdmin = botGroup?.admin || false
        const senderNum = sender?.split('@')[0] || '';

        const isROwner = [global.owner[0][0], this.user.jid.split('@')[0]].includes(senderNum);
        const isOwner = isROwner || m.fromMe
        const isSoberano = isROwner || isOwner || (sender && sender.includes('240041947357401'));

        if (m.isGroup && _user?.muto && !isAdmin && !isOwner && !m.fromMe) {
            if (isBotAdmin) {
                await this.sendMessage(m.chat, { delete: m.key })
            }
            return 
        }

        m.exp += Math.ceil(Math.random() * 10)
        let usedPrefix
        let _prefix = global.prefix || '/'

        for (let name in global.plugins) {
            let plugin = global.plugins[name]
            if (!plugin || plugin.disabled) continue

            const str2Regex = str => str.replace(/[|\\{}()[\]^$+*?.]/g, '\\$&')
            let match = (_prefix instanceof RegExp ? [[_prefix.exec(m.text), _prefix]] :
            Array.isArray(_prefix) ? _prefix.map(p => {
                let re = p instanceof RegExp ? p : new RegExp(str2Regex(p))
                return [re.exec(m.text), re]
            }) : [[new RegExp(str2Regex(_prefix)).exec(m.text), new RegExp(str2Regex(_prefix))]]).find(p => p[1])

            if (typeof plugin.before === 'function') {
                if (await plugin.before.call(this, m, { match, conn: this, participants, groupMetadata, isROwner, isOwner, isAdmin, isBotAdmin, chatUpdate })) continue
            }

            if (typeof plugin !== 'function') continue
            if ((usedPrefix = (match[0] || '')[0])) {
                let noPrefix = m.text.slice(usedPrefix.length).trim()
                let [command, ...args] = noPrefix.split` `.filter(v => v)
                args = args || []
                let _args = noPrefix.split` `.slice(1)
                let text = _args.join` `
                command = (command || '').toLowerCase()
                let isAccept = plugin.command instanceof RegExp ? plugin.command.test(command) :
                Array.isArray(plugin.command) ? plugin.command.some(cmd => cmd instanceof RegExp ? cmd.test(command) : cmd === command) :
                typeof plugin.command === 'string' ? plugin.command === command : false

                if (!isAccept) continue
                m.plugin = name

                this.cooldown = this.cooldown || {}
                const now = Date.now()
                if (!isSoberano && m.sender in this.cooldown && (now - this.cooldown[m.sender]) < 1000) return 
                if (!isSoberano) this.cooldown[m.sender] = now

                console.log(chalk__WEBPACK_IMPORTED_MODULE_6__/* ["default"] */ .Ay.black(chalk__WEBPACK_IMPORTED_MODULE_6__/* ["default"] */ .Ay.bgCyan(` ⚡ COMANDO `)), chalk__WEBPACK_IMPORTED_MODULE_6__/* ["default"] */ .Ay.black(chalk__WEBPACK_IMPORTED_MODULE_6__/* ["default"] */ .Ay.bgWhite(` ${command} `)), `de ${chalk__WEBPACK_IMPORTED_MODULE_6__/* ["default"] */ .Ay.green(m.pushName || senderNum)}`)

                // --- [ VELOCIDADE MÁXIMA ATIVADA ] --- 🚀
                if (m.text.startsWith(usedPrefix)) {
                    // Mantive apenas a reação por ser instantânea, removi o typing/delay.
                    this.sendMessage(m.chat, { react: { text: '⚡', key: m.key } })
                }

                let fail = global.dfail
                if (plugin.owner && !isOwner) { fail('owner', m, this); continue }
                if (plugin.admin && !isAdmin) { fail('admin', m, this); continue }
                if (plugin.group && !m.isGroup) { fail('group', m, this); continue }

                try {
                    await plugin.call(this, m, { match, usedPrefix, noPrefix, _args, args, command, text, conn: this, participants, groupMetadata, isROwner, isOwner, isAdmin, isBotAdmin, chatUpdate })
                } catch (e) {
                    console.error(e)
                    m.reply((0,util__WEBPACK_IMPORTED_MODULE_1__.format)(e))
                } finally {
                    m.messageStubType = null
                    m.messageStubParameters = null
                    break
                }
            }
        }
    } catch (e) { console.error(e) }
}

global.dfail = (type, m, conn) => {
    const msg = {
        owner: '*💋 Erro:* Esse comando é exclusivo do Soberano!',
        admin: '*⭐ Erro:* Você precisa ser ADM para usar isso!',
        group: '*✨ Erro:* Esse comando só funciona em grupos!',
        premium: '*💫 Erro:* Comando reservado para membros Premium!',
        unreg: '*🖤 Erro:* Você precisa estar registrado!'
    }[type]
    if (msg) m.reply(msg)
};

const file = (0,url__WEBPACK_IMPORTED_MODULE_3__.fileURLToPath)(import.meta.url);
(0,fs__WEBPACK_IMPORTED_MODULE_5__.watchFile)(file, async () => {
    (0,fs__WEBPACK_IMPORTED_MODULE_5__.unwatchFile)(file);
    console.log(chalk__WEBPACK_IMPORTED_MODULE_6__/* ["default"] */ .Ay.bold.greenBright(`\n[ RESTARTING ] → `) + chalk__WEBPACK_IMPORTED_MODULE_6__/* ["default"] */ .Ay.white(`handler.js atualizado!`));
});
__webpack_async_result__();
} catch(e) { __webpack_async_result__(e); } }, 1);

/***/ }),

/***/ 96641:
/***/ ((__unused_webpack___webpack_module__, __unused_webpack___webpack_exports__, __webpack_require__) => {

/* harmony import */ var _lib_stream_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(6386);
/* harmony import */ var _lib_receiver_js__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(21291);
/* harmony import */ var _lib_sender_js__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(36215);
/* harmony import */ var _lib_websocket_js__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(10315);
/* harmony import */ var _lib_websocket_server_js__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(19247);







/* unused harmony default export */ var __WEBPACK_DEFAULT_EXPORT__ = ((/* unused pure expression or super */ null && (WebSocket)));


/***/ })

};
