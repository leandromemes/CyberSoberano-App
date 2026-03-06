#!/bin/bash
##--------------------------------------------------------------------
## PROJETO: CyberSoberano - Terminal para Bots
## AUTOR: Soberano
## GRUPO: 🌀 COMPLEXO 🌀
## DESCRIÇÃO: Auditoria de repositórios e pacotes para ambiente de Bots
##--------------------------------------------------------------------

# Função para listar repositórios configurados
repositorios_assinados() {
	local fontes_principais
	# Procura por fontes ativas no sources.list do CyberSoberano
	fontes_principais=$(grep -P '^\s*deb\s' "@TERMUX_PREFIX@/etc/apt/sources.list")

	if [ -n "$fontes_principais" ]; then
		echo "#### Lista de Fontes (Principal)"
		echo "\`$fontes_principais\`"
	fi

	local nome_arquivo pacote_repo fontes_supl
	while read -r nome_arquivo; do
		pacote_repo=$(dpkg -S "$nome_arquivo" 2>/dev/null | cut -d : -f 1)
		fontes_supl=$(grep -P '^\s*deb\s' "$nome_arquivo")

		if [ -n "$fontes_supl" ]; then
			if [ -n "$pacote_repo" ]; then
				echo "#### $pacote_repo (Fontes Adicionais: $(basename "$nome_arquivo"))"
			else
				echo "#### Fontes Adicionais: $(basename "$nome_arquivo")"
			fi
			echo "\`$fontes_supl\`  "
		fi
	done < <(find "@TERMUX_PREFIX@/etc/apt/sources.list.d" -maxdepth 1 ! -type d)
}

# Função para listar pacotes que precisam de atualização (Essencial para manter o Bot estável)
pacotes_atualizaveis() {
	local lista_atualizaveis

	if [ "$(id -u)" = "0" ]; then
		echo "Executando como root. O APT pode ter restrições para verificar atualizações."
	else
		# Tenta atualizar a lista silenciosamente
		apt update >/dev/null 2>&1
		lista_atualizaveis=$(apt list --upgradable 2>/dev/null | tail -n +2)

		if [ -z "$lista_atualizaveis" ];then
			echo "Todos os pacotes de suporte ao Bot estão atualizados."
		else
			echo "Atenção: Existem pacotes que podem ser otimizados:"
			echo $'```\n'"$lista_atualizaveis"$'\n```\n'
		fi
	fi
}

# Saída Final formatada para o Terminal do CyberSoberano
saida="
### [ AUDITORIA DE SISTEMA - CYBERSOBERANO ]

$(repositorios_assinados)
##

### [ STATUS DE ATUALIZAÇÃO DOS COMPONENTES ]

$(pacotes_atualizaveis)
##
"

echo "$saida"