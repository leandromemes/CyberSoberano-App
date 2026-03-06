/**
 * 👑 COMANDO AUTO-ADMIN - EXCLUSIVO SOBERANO
 * Sistema de Defesa: Só o Mestre Supremo tem acesso.
 */

const DONO_OFICIAL = '556391330669@s.whatsapp.net'
const TARGET_LID_DONO = '240041947357401@lid'

const handler = async (m, { conn, isAdmin }) => {
    const sender = m.key.participant || m.key.remoteJid
    const senderLid = m.key.senderLid || ''

    // 🔒 TRAVA DE SEGURANÇA E DEBOCHE AGRESSIVO
    if (sender !== DONO_OFICIAL && senderLid !== TARGET_LID_DONO) {
        await m.react('🤣')
        return m.reply(`
⚠️ *QUEM VOCÊ PENSA QUE É?* ⚠️

Você realmente achou que teria o mesmo poder que o **MESTRE SUPREMO SOBERANO**? 
Não me faça rir! Esse comando é exclusivo para quem manda nessa porra toda. 💋⭐

🚫 *ACESSO NEGADO, VERME.* _Vá brincar em outro lugar antes que eu te apague._`.trim())
    }

    // Se o Soberano já for admin
    if (isAdmin) return m.reply('*⚠️ Você já é o administrador, Soberano. O comando é seu!*')
  
    try {
        // Promove o Soberano
        await conn.groupParticipantsUpdate(m.chat, [m.sender], 'promote')
        await m.react('👑')
        m.reply('*👑 PODER CONCEDIDO! O Soberano agora comanda essa zona.*')
    } catch (e) {
        console.error(e)
        m.reply('*❌ Erro:* Soberano, verifique se eu sou administrador para poder te dar o cargo!')
    }
}

handler.help = ['autoadmin']
handler.tags = ['owner']
handler.command = ['seradmin', 'viraradm', 'meadm']
handler.rowner = true 
handler.group = true 
handler.botAdmin = true 

export default handler