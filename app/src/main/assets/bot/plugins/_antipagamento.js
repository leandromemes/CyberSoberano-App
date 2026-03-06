/**
 * 🛡️ SENTINELA ANTI-FRAUDE (VERSÃO ULTRA-SENSÍVEL)
 * @author Leandro Rocha
 * @project CYBERSOBERANO 💋⭐✨💫🌙🖤
 */

export async function before(m, { conn, isAdmin, isBotAdmin, isOwner, isROwner }) {
    if (!m.isGroup) return !0
    // O Dono e ADMs são imunes para não serem banidos durante testes
    if (isAdmin || isOwner || m.fromMe || isROwner) return !0

    let chat = global.db.data.chats[m.chat]
    if (!chat.antipagamento) return !0

    // Detecta qualquer vestígio de mensagem de pagamento ou pedido (Ghost Payment)
    const paymentTypes = ['paymentInviteMessage', 'requestPaymentMessage', 'orderMessage', 'paymentMessage']
    const isPaymentSpam = paymentTypes.includes(m.mtype) || 
                          !!m.message?.paymentInviteMessage || 
                          !!m.message?.requestPaymentMessage

    if (isPaymentSpam) {
        const user = `@${m.sender.split`@`[0]}`
        
        await conn.reply(m.chat, `*「 🛡️ PROTEÇÃO SOBERANA 」*\n\n《✧》${user} Detectado envio de pagamento/spam. Seguindo as ordens do mestre, você foi removido. 💀`, m, { mentions: [m.sender] })

        if (isBotAdmin) {
            // Apaga a mensagem (limpeza de rastro)
            await conn.sendMessage(m.chat, { delete: m.key })
            // Expulsa o invasor
            await conn.groupParticipantsUpdate(m.chat, [m.sender], 'remove')
        }
        return !1 // Bloqueia o processamento
    }
    return !0
}