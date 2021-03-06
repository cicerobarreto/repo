package com.poc.mbeans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.poc.entity.LoginData;
import com.poc.entity.Pessoa;
import com.poc.entity.TipoPermissao;
import com.poc.service.PessoaService;
import com.poc.util.ApplicationResources;
import com.poc.util.FacesUtil;
import com.poc.util.SessionManager;

@ManagedBean
@ViewScoped
public class PessoaBean extends BaseBean implements Serializable {

	protected final Logger log = LogManager.getLogger(this.getClass());

	private static final long serialVersionUID = 1L;

	//Utilizada na view "cadastro"
	private Pessoa pessoaCadastro = new Pessoa();
	
	//Utilizada na view "lista"
	private Pessoa pessoaSelecionada;

	@EJB
	private PessoaService pessoaService;

	public String cadastrar() {

		//Insere a Pessoa
		pessoaCadastro.setDataUltimoAcesso(new Date());
		pessoaService.inserirPessoa(getPessoaCadastro());

		// Adiciona a pessoa � sess�o
		setSessionUser(getPessoaCadastro());
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
		SessionManager.getInstace().registerLogin(request.getSession().getId(),
				new LoginData(getPessoaCadastro(), getPessoaCadastro().getDataUltimoAcesso(), request.getSession().getId()));


		FacesUtil.adicionarMensagemSucesso(ApplicationResources.PESSOA_SUCESSO_INSERCAO,
				pessoaCadastro.getNome());

		// Reinicia os valores do objeto, limpando os campos do formul�rio.
		pessoaCadastro = new Pessoa();

		return "index";
	}
	
	public List<Pessoa> getListaPessoas() {
		return pessoaService.getAll();
	}
	
	public void excluirPessoaSelecionada() {
		pessoaService.excluirPessoa(getPessoaSelecionada().getId());	    	    
	}
	
	public void alterarPessoaSelecionada() {
		pessoaService.alterarPessoa(pessoaSelecionada);
		
		FacesUtil.adicionarMensagemSucesso("Pessoa Alterada.");
	}
	
	public List<TipoPermissao> getTiposPermissao() {
		return Arrays.asList(TipoPermissao.values());
	}

	// Get & Set
	public Pessoa getPessoaCadastro() {
		return pessoaCadastro;
	}

	public void setPessoaCadastro(Pessoa pessoaCadastro) {
		this.pessoaCadastro = pessoaCadastro;
	}

	public Pessoa getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(Pessoa pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
	}
}
