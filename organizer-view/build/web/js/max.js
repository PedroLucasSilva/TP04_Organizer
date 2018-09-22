/* 
    Created on : 30/07/2018, 11:12:19
    Author     : Ruan Bertuce
*/

/* VARIÁVEIS QUE O MAX UTILIZA */

// --| Variáveis para controle de itens |-- 
//( Elas tem três estados: 0 - estado inicial, null - são nulas, ? - qualquer outra coisa )

var itemType = 0; // Esta assume três valores: Simples, Tarefa, Lembrete
var itemName = 0;
var itemDescription = 0;
var itemDate = 0;
var tagName = 0;

// Variável para controlar a ação que está sendo invocada pelo usuário - Assume valores de 1 a 20 
var action = 0; 

// Variável para controlar respostas do servidor
var response = null;

// --| Simulação do banco de dados |--

// Preparando estrutura
var itemsID = [];
var itemsType = [];
var itemsName = [];
var itemsDescription = [];
var itemsDate = [];
var itemsStatus = [];
var tagsID = [];
var tagsName = [];
var tagsItems = [];
var itemsTags = []; 

/*FUNÇÕES QUE O MAX UTILIZA*/

// --| Funções de compreensão |-- 

//Função de conversão de String para o formato que o MAX entende

function convertString( str ){
    if(typeof str === "string"){
        return str.replace( str.charAt(0), str.charAt(0).toUpperCase() );
    }else{
        alert(str);
        return str;
    }
}

// - Entendimento-Associação de palavras - 

function compare( str1, str2 ){ 
    var result = 0;
    str1 = convertString( str1 );
    str2 = convertString( str2 );
    if( str1.localeCompare( str2 ) != 0 ) result = 1;
    return result;
}

// - Entendimento-conversão de datas -

function convertDate( txtDate ){
    if( txtDate.indexOf( " de " ) == -1 ) return 1;
    var date = txtDate.split( " de " );

    //convertendo dia
    if( compare( date[0], "1" ) == 0 ) date[0] = "01";
    else if( compare( date[0], "2" ) == 0 ) date[0] = "02";
    else if( compare( date[0], "3" ) == 0 ) date[0] = "03";
    else if( compare( date[0], "4" ) == 0 ) date[0] = "04";
    else if( compare( date[0], "5" ) == 0 ) date[0] = "05";
    else if( compare( date[0], "6" ) == 0 ) date[0] = "06";
    else if( compare( date[0], "7" ) == 0 ) date[0] = "07";
    else if( compare( date[0], "8" ) == 0 ) date[0] = "08";
    else if( compare( date[0], "9" ) == 0 ) date[0] = "09";

    //convertendo mês
    if( compare( date[1], "janeiro" ) == 0 ) date[1] = "01";
    else if( compare( date[1], "fevereiro" ) == 0 ) date[1] = "02";
    else if( compare( date[1], "março" ) == 0 ) date[1] = "03";
    else if( compare( date[1], "abril" ) == 0 ) date[1] = "04";
    else if( compare( date[1], "maio" ) == 0 ) date[1] = "05";
    else if( compare( date[1], "junho" ) == 0 ) date[1] = "06";
    else if( compare( date[1], "julho" ) == 0 ) date[1] = "07";
    else if( compare( date[1], "agosto" ) == 0 ) date[1] = "08";
    else if( compare( date[1], "setembro" ) == 0 ) date[1] = "09";
    else if( compare( date[1], "outubro" ) == 0 ) date[1] = "10";
    else if( compare( date[1], "novembro" ) == 0 ) date[1] = "11";
    else if( compare( date[1], "dezembro" ) == 0 ) date[1] = "12";
    else return 1;

    //convertendo ano
    if( date.length == 2 ) date[2] = new Date().getFullYear();

    if( isNaN( parseInt( date[0] ) ) || isNaN( parseInt( date[1] ) ) ) return 1;

    if( parseInt( date[0] ) < 0 || parseInt( date[0] ) > 31 || parseInt( date[2] ) < 0 || 
        parseInt( date[0] ) > 29 && parseInt( date[1] ) == 2 )
        return 1;

    //retornando a conversão concatenada
    return date[0] + "-" + date[1] + "-" + date[2];
}

// --| Função de conversão para o formato do banco de dados |--

// conversão de data

function convertDateToBDFormat( date ){
    if( date == null ) return null;
    date = date.split("-");
    return date[2] + "-" + date[1] + "-" + date[0];
}

function convertDateToMaxFormat( date ){
    if( date == null ) return null;
    date = date.split("");
    date.splice(5, 1);
    date = date.join("");
    date = date.split(" ");

    if( compare( date[0], "jan" ) == 0 ) date[0] = "01";
    else if( compare( date[0], "fev" ) == 0 ) date[0] = "02";
    else if( compare( date[0], "mar" ) == 0 ) date[0] = "03";
    else if( compare( date[0], "abr" ) == 0 ) date[0] = "04";
    else if( compare( date[0], "mai" ) == 0 ) date[0] = "05";
    else if( compare( date[0], "jun" ) == 0 ) date[0] = "06";
    else if( compare( date[0], "jul" ) == 0 ) date[0] = "07";
    else if( compare( date[0], "ago" ) == 0 ) date[0] = "08";
    else if( compare( date[0], "set" ) == 0 ) date[0] = "09";
    else if( compare( date[0], "out" ) == 0 ) date[0] = "10";
    else if( compare( date[0], "nov" ) == 0 ) date[0] = "11";
    else if( compare( date[0], "dez" ) == 0 ) date[0] = "12";
    return date[1] + "-" + date[0] + "-" + date[2];
}

function convertedItemsType(){
    var array = itemsType;
    for( i = 0; i < array.length; i++){
        if( array[i].localeCompare("Simples") == 0 ) array[i] = "SIM";
        else if( array[i].localeCompare("Tarefa") == 0 ) array[i] = "TAR";
        else array[i] = "LEM";
    }
    return array;
}

function convertedItemsDate(){
    var array = itemsDate;
    for( i = 0; i < array.length; i++){
        array[i] = convertDateToBDFormat( array[i] );
    }
    return array;
}

function convertedTagsItems(){
    if( tagsItems == null ) return null;
    var array = tagsItems;
    for( i = 0; i < array.length; i++){
        for( j = 0; j < itemsName.length; j++ ){
            if( compare( array[i], itemsName[j] ) == 0 ) array[i] = itemsID[j];
        }
    }
    return array;
}

function convertedItemsTags(){
    if( itemsTags == null ) return null;
    var array = itemsTags;
    for( i = 0; i < array.length; i++){ 
        for( j = 0; j < tagsName.length; j++ ){ 
            if( compare( array[i], tagsName[j] ) == 0 ) array[i] = tagsID[j];
        }
    }
    return array;
}

// --| Função de carregamento de dados |--
function loadMax(){

    if(!jsonItem) jsonItem = null;
    else{ 
        for (var i = 0; i < jsonItem.length; i++) {
            itemsID[i] = jsonItem[i].seqItem;

            if( jsonItem[i].identifierItem.localeCompare("SIM") == 0 ) itemsType[i] = "Simples";
            else if( jsonItem[i].identifierItem.localeCompare("TAR") == 0 ) itemsType[i] = "Tarefa";
            else itemsType[i] = "Lembrete";
    
            itemsName[i] = convertString( jsonItem[i].nameItem );

            if(!jsonItem[i].descriptionItem) itemsDescription[i] = null;
            else itemsDescription[i] = jsonItem[i].descriptionItem;

            if(!jsonItem[i].dateItem) itemsDate[i] = null;
            else itemsDate[i] = convertDateToMaxFormat( jsonItem[i].dateItem ) ;

            if(!jsonItem[i].identifierStatus) itemsStatus[i] = null;
            else itemsStatus[i] = jsonItem[i].identifierStatus;
        }
    }
    if(!jsonTag) jsonTag = null; 
    else{
        for (var i = 0; i < jsonTag.length; i++) {
                tagsID[i] = jsonTag[i].seqTag;
                tagsName[i] = convertString( jsonTag[i].tagName );
        }
    }
    if( !jsonTagsItems || !jsonItemsTags ){ jsonTagsItems = null; jsonItemsTags = null; }
    else{ 
        for (var i = 0; i < jsonTagsItems.length; i++) {
            tagsItems[i] = convertString( jsonTagsItems[i] );        
            itemsTags[i] = convertString( jsonItemsTags[i] );
        }
    }
}
loadMax();

function updateBD() {

    $.ajax({
        url : '/organizer/servletcontroller',
        type: "POST",
        data : {
            process : "UpdateMaxBD",
            itemsID  : JSON.stringify( itemsID ),
            itemsType : JSON.stringify( convertedItemsType( itemsType ) ),
            itemsName : JSON.stringify( itemsName ),
            itemsDescription : JSON.stringify( itemsDescription ),
            itemsDate : JSON.stringify( convertedItemsDate( itemsDate ) ),
            itemsStatus : JSON.stringify( itemsStatus ),
            tagsID : JSON.stringify( tagsID ),
            tagsName : JSON.stringify( tagsName ),
            tagsItems : JSON.stringify( convertedTagsItems( tagsItems ) ),
            itemsTags : JSON.stringify( convertedItemsTags( itemsTags ) )
        },
        success : function(responseText) {
            window.location.href = "/organizer/servletcontroller?process=LoadItem";
        }
    }); 
}

// --| Funções de verificação |--

//se um item existe

function itemNameExists( itemName ){
    if( itemsName.indexOf( convertString( itemName ) ) == -1 ) return 0;
    return 1;
}

//se um item possui a propriedade data

function itemDatePropertyExists( itemName ){
    if( compare( itemsType[ itemsName.indexOf( convertString( itemName ) ) ], "simples" ) == 0 ) return 0;
    return 1;
}

//se uma tag existe

function tagNameExists( tagName ){
    if( tagsName.indexOf( convertString( tagName ) ) == -1 ) return 0;
    return 1;
}

//se um item específico possui uma tag específica

function itemTagExists( itemName, tagName ){
    for ( i = 0; i < tagsItems.length; i++ ) {
        if( compare( tagsItems[i], itemName ) == 0 && compare( itemsTags[i], tagName ) == 0 )
            return 1;
    }
    return 0;
}

//se um item especifíco possui alguma tag qualquer

function itemTagsExist( itemName ){
    if( tagsItems.indexOf( convertString( itemName ) ) == -1 ) return 0;
    return 1;
}

//se uma tag específica está associada a algum item

function tagItemsExist( tagName ){
    if( itemsTags.indexOf( convertString( tagName ) ) == -1 ) return 0;
    return 1;
}

// --| Funções de ação |--

// - Criação de um item -

function createItem( itemType, itemName, itemDescription, itemDate ){
    itemsID[itemsID.length] = "default";
    itemsType[itemsType.length] = convertString( itemType );
    itemsName[itemsName.length] = convertString( itemName );
    if( itemDescription == null ) itemsDescription[itemsDescription.length] = itemDescription;
    else itemsDescription[itemsDescription.length] = convertString( itemDescription );
    itemsDate[itemsDate.length] = itemDate;
    itemsStatus[itemsStatus.length] = null;
    return 0;
}

// - Alteração de uma propriedade de um item -

//Nome

function alterItemName( itemName, newName ){
    itemsName[itemsName.indexOf( convertString( itemName ) )] = convertString( newName );
    return 0;
}

//Descrição

function alterItemDescription( itemName, newDescription ){
    if( compare( newDescription, "nenhuma" ) == 0 ){
        itemsDescription[itemsName.indexOf( convertString( itemName ) )] = null;
    } 
    else itemsDescription[itemsName.indexOf( convertString( itemName ) )] = convertString( newDescription );
    return 0;
}

//Data

function alterItemDate( itemName, newDate ){
    if( compare( newDate, "nenhuma" ) == 0 ) newDate = null;
    itemsDate[itemsName.indexOf( convertString( itemName ) )] = newDate;
    return 0;
}

// - Exclusão de um item - 

//por nome

function deleteItem( itemName ){
    var i = itemsName.indexOf( convertString( itemName ) );
    itemsID.splice( i, 1 );
    itemsType.splice( i, 1);
    itemsName.splice( i, 1 );
    itemsDescription.splice( i, 1 );
    itemsDate.splice( i, 1 );
    itemsStatus.splice( i, 1 );
    for( i = tagsItems.length-1; i >= 0; i-- ){
        if( compare( tagsItems[i], itemName ) == 0 ){
            tagsItems.splice(i, 1);
            itemsTags.splice(i, 1);
        }
    }
    return 0;
}

//por tag

function deleteTagItems( tagName ){
    var result = 1;
    for ( i = itemsTags.length-1; i >= 0; i-- ) {
        if( compare( itemsTags[i], tagName ) == 0 ){
            deleteItem( tagsItems[i] );
            result = 0;
        } 
    }
    return result;
}

// - Pesquisa de itens -

//todos

function searchItems(){
    var results = [];
    for( i=0; i<itemsName.length; i++ ) results[i] = i;
    return results;
}

//pelo nome e pela descrição

function searchItem( keyword ){
    var results = [];
    for( i=0; i<itemsName.length; i++ ){
        //nome
        if( itemsName[i].toLowerCase().indexOf( keyword.toLowerCase() ) != -1 ){
            //caso "keyword"
            if( itemsName[i].toLowerCase().indexOf( keyword.toLowerCase() ) == 0 &&
                itemsName[i].toLowerCase().indexOf( keyword.toLowerCase() )+keyword.length-1 == 
                itemsName[i].length-1 )
                results[results.length] = i;

            //caso "keyword "
            if( itemsName[i].toLowerCase().indexOf( keyword.toLowerCase() ) == 0 &&
                itemsName[i].charAt( keyword.length ) == " " ) 
                results[results.length] = i;

            //caso " keyword "
            if( itemsName[i].charAt( itemsName[i].indexOf( keyword.toLowerCase() )-1 ) == " " &&
                itemsName[i].charAt( itemsName[i].indexOf( keyword.toLowerCase() )+keyword.length ) == " " ) 
                results[results.length] = i;

            //caso " keyword"
            if( itemsName[i].charAt( itemsName[i].indexOf( keyword.toLowerCase() )-1 ) == " " &&
                itemsName[i].toLowerCase().indexOf( keyword.toLowerCase() )+keyword.length-1 == 
                itemsName[i].length-1 ) 
                results[results.length] = i;
        }

        //descrição
        if( itemsDescription[i] != null ){

            if( itemsDescription[i].toLowerCase().indexOf( keyword.toLowerCase() ) != -1 ){
                //caso "keyword"
                if( itemsDescription[i].toLowerCase().indexOf( keyword.toLowerCase() ) == 0 &&
                    itemsDescription[i].toLowerCase().indexOf( keyword.toLowerCase() )+keyword.length-1 
                    == itemsDescription[i].length-1 )
                    results[results.length] = i;

                //caso "keyword "
                if( itemsDescription[i].toLowerCase().indexOf( keyword.toLowerCase() ) == 0 &&
                    itemsDescription[i].charAt( keyword.length ) == " " ) 
                    results[results.length] = i;

                //caso " keyword "
                if( itemsDescription[i].charAt( itemsDescription[i].indexOf( keyword.toLowerCase() )-1 ) == " " &&
                    itemsDescription[i].charAt( itemsDescription[i].indexOf( keyword.toLowerCase() )+keyword.length ) 
                    == " " ) 
                    results[results.length] = i;

                //caso " keyword"
                if( itemsDescription[i].charAt( itemsDescription[i].indexOf( keyword.toLowerCase() )-1 ) == " " &&
                    itemsDescription[i].toLowerCase().indexOf( keyword.toLowerCase() )+keyword.length-1 
                    == itemsDescription[i].length-1 ) 
                            results[results.length] = i;
            }    
        }                   
    }
    return results;
}

//pelo tipo

function searchItemByType( keyword ){
    var results = [];
    for( i=0; i<itemsType.length; i++ ){
        if( itemsType[i].toLowerCase().indexOf( keyword.toLowerCase() ) == 0 )
            results[results.length] = i;
    }
    return results;
}

//pelo nome

function searchItemByName( keyword ){
    var results = [];
    for( i=0; i<itemsName.length; i++ ){
        if( itemsName[i].toLowerCase().indexOf( keyword.toLowerCase() ) != -1 ){
            //caso "keyword"
            if( itemsName[i].toLowerCase().indexOf( keyword.toLowerCase() ) == 0 &&
                itemsName[i].toLowerCase().indexOf( keyword.toLowerCase() )+keyword.length-1 
                == itemsName[i].length-1 )
                results[results.length] = i;

            //caso "keyword "
            if( itemsName[i].toLowerCase().indexOf( keyword.toLowerCase() ) == 0 &&
                itemsName[i].charAt( keyword.length ) == " " ) 
                results[results.length] = i;

            //caso " keyword "
            if( itemsName[i].charAt( itemsName[i].indexOf( keyword.toLowerCase() )-1 ) == " " &&
                itemsName[i].charAt( itemsName[i].indexOf( keyword.toLowerCase() )+keyword.length ) == " " ) 
                results[results.length] = i;

            //caso " keyword"
            if( itemsName[i].charAt( itemsName[i].indexOf( keyword.toLowerCase() )-1 ) == " " &&
                itemsName[i].toLowerCase().indexOf( keyword.toLowerCase() )+keyword.length-1 == 
                itemsName[i].length-1 ) 
                results[results.length] = i;
        }
    }
    return results;
}

//pela descrição

function searchItemByDescription( keyword ){
    var results = [];
    for( i=0; i<itemsDescription.length; i++ ){
        if( itemsDescription[i] != null ){
            if( itemsDescription[i].toLowerCase().indexOf( keyword.toLowerCase() ) != -1 ){
                //caso "keyword"
                if( itemsDescription[i].toLowerCase().indexOf( keyword.toLowerCase() ) == 0 &&
                    itemsDescription[i].toLowerCase().indexOf( keyword.toLowerCase() )+keyword.length-1 
                    == itemsDescription[i].length-1 )
                    results[results.length] = i;

                //caso "keyword "
                if( itemsDescription[i].toLowerCase().indexOf( keyword.toLowerCase() ) == 0 &&
                    itemsDescription[i].charAt( keyword.length ) == " " ) 
                    results[results.length] = i;

                //caso " keyword "
                if( itemsDescription[i].charAt( itemsDescription[i].indexOf( keyword.toLowerCase() )-1 ) == " " &&
                    itemsDescription[i].charAt( itemsDescription[i].indexOf( keyword.toLowerCase() )+keyword.length ) 
                    == " " ) 
                    results[results.length] = i;

                //caso " keyword"
                if( itemsDescription[i].charAt( itemsDescription[i].indexOf( keyword.toLowerCase() )-1 ) == " " &&
                    itemsDescription[i].toLowerCase().indexOf( keyword.toLowerCase() )+keyword.length-1 
                    == itemsDescription[i].length-1 ) 
                    results[results.length] = i;
            }    
        }                   
    }
    return results;
}

//pela data

function searchItemByDate( keyword ){
    var results = [];
    for( i=0; i<itemsDate.length; i++ ){
        if( itemsDate[i].localeCompare( keyword ) == 0 )
            results[results.length] = i;
    }
    return results;
}

//pela tag

function searchItemByTag( keyword ){
    var results = [];
    for( i=0; i<itemsTags.length; i++ ){
        if( compare( itemsTags[i], keyword ) == 0 ){
            for( j=0; j<itemsName.length; j++ ){
                if( compare( tagsItems[i], itemsName[j] ) == 0 )
                    results[results.length] = j;
            }
        }
    }
    return results;
}

//Construção dos resultados da pesquisa

function buildSearchResults( results ){
    var builtResults = "\n";
    for( i=0; i<results.length; i++ ){
        builtResults +=      
        "\n" +" | "+ itemsName[results[i]] + " | " + "\n" +
        "\n> Descrição: " + itemsDescription[results[i]] +
        "\n> Data: " + itemsDate[results[i]] + 
        "\n> Tipo: " + itemsType[results[i]];
        if( itemTagsExist( itemsName[results[i]] ) == 1 ){
            builtResults += "\n> Tag(s): ";
            for( j=0; j<tagsItems.length; j++ ){
                if( compare( tagsItems[j], itemsName[results[i]] ) == 0 ){
                    builtResults += itemsTags[j] + "; ";
                }
            }  
        }        
        builtResults += "\n";
    } 
    return builtResults; 
}

// - Criação de uma tag - 

function createTag( tagName ){
    tagsID[tagsID.length] = "default";
    tagsName[tagsName.length] = convertString( tagName );
    return 0;
}

// - Alteração de um tag - 

function alterTagName( tagName, newName ){
    tagsName[tagsName.indexOf( convertString( tagName ) )] = convertString( newName );
    for ( i=0; i < itemsTags.length; i++ ) {
        if( compare( itemsTags[i], tagName ) == 0 ) itemsTags[i] = convertString( newName );
    }
    return 0;
}

// - Exclusão de uma tag -

function deleteTag( tagName ){
    tagsID.splice( tagsName.indexOf( convertString( tagName ) ), 1 );
    tagsName.splice( tagsName.indexOf( convertString( tagName ) ), 1 );
    for ( i=tagsItems.length-1; i>=0; i-- ) {
            if( compare( itemsTags[i], tagName ) == 0 ){
                tagsItems.splice( i, 1 );
                itemsTags.splice( i, 1 );
            }
    }
    return 0;
}

// - Adição de uma tag à um item -

function addTagToItem( itemName, tagName ){
    tagsItems[ tagsItems.length ] = convertString( itemName );
    itemsTags[ itemsTags.length ] = convertString( tagName );
    return 0;
}

// - Remoção de uma tag de um item -

function removeTagFromItem( itemName, tagName ){
    for ( i = itemsTags.length-1; i>=0; i-- ) {
        if( compare( itemsTags[i], itemName ) == 0 && compare( tagsItems[i], tagName ) == 0 ){
            tagsItems.splice( i, 1 );
            itemsTags.splice( i, 1 );
        }
    }
    return 0;
}

/* VOZ E DECISÕES DO MAX - TEXT-TO-SPEECH */

var transcription = null; 

//Inicia o controlador speechSynthesis
var synth = window.speechSynthesis;

//Configura as características da voz
var myVoice = new SpeechSynthesisUtterance(); 
myVoice.pitch = 0.5; //grave - agudo
myVoice.rate = 1; //devagar - rápido
myVoice.volume = 1;

//Decide as falas do MAX de acordo com o que foi reconhecido
function sayMax() {
    txtTranscripted = document.getElementById( 'transcription' ).value;

    if( txtTranscripted.localeCompare("") == 0 ){
        myVoice.text = "";
        document.getElementById( 'speech' ).value = "";
    } else {
        myVoice.text = "Me desculpe, mas não fui programado para reagir à isso."; //Fala default
        document.getElementById( 'speech' ).value = "Me desculpe, mas não fui programado para reagir à isso.";
    }   

    //INTERAÇÕES BÁSICAS
    if( compare( txtTranscripted, "olá" ) == 0 ){
        document.getElementById( 'speech' ).value = "Olá! Meu nome é MAX! Prazer em conhecer você!";
        myVoice.text = "Olá! Meu nome é MAX! Prazer em conhecer você!";
    }            
    if( compare( txtTranscripted, "quem é você" ) == 0 ){
        document.getElementById( 'speech' ).value = "Eu sou o MAX: o Módulo Assistente X do organizer. ";
        myVoice.text = "Eu sou o MAX: o Módulo Assistente dez do organizer. ";
    }           
    if( compare( txtTranscripted, "como você pode me ajudar" ) == 0 ){
        document.getElementById( 'speech' ).value = "Eu possuo dez tipos de interações que incluem criar, alterar, excluir e pesquisar itens. Além de criar, alterar e excluir tags, podendo também adicioná-las e removê-las de itens.";
        myVoice.text = "Eu possuo dez tipos de interações que incluem criar, alterar, excluir e pesquisar itens. Além de criar, alterar e excluir tags, podendo também adicioná-las e removê-las de itens.";
    }
    if( compare( txtTranscripted, "obrigado" ) == 0 ){
        document.getElementById( 'speech' ).value = "Por nada! Estou sempre a disposição! Na dúvida, Organize!";
        myVoice.text = "Por nada! Estou sempre a disposição! Na dúvida, Organize!";
    }     

    //CRIAR ITEM

    //item simples - action 1

    if( action == 1 && itemName != 0 ){
        if( compare( txtTranscripted, "nenhuma" ) != 0 ){
            itemDescription = txtTranscripted;

            if( createItem( itemType, itemName, itemDescription, null ) == 0 ){
                document.getElementById( 'speech' ).value = "Item criado com sucesso!";
                myVoice.text = "Item criado com sucesso!";
                updateBD();
            } 
            else {
                document.getElementById( 'speech' ).value = "Houve uma falha na criação do item!";
                myVoice.text = "Houve uma falha na criação do item!";
            }

            itemType = 0;
            itemName = 0;
            itemDescription = 0;    
        }else{

            if( createItem( itemType, itemName, null, null ) == 0 ){
                document.getElementById( 'speech' ).value = "Item criado com sucesso!";
                myVoice.text = "Item criado com sucesso!";
                updateBD();
            } 
            else{
                document.getElementById( 'speech' ).value = "Houve uma falha na criação do item!";
                myVoice.text = "Houve uma falha na criação do item!";
            }
            itemType = 0;
            itemName = 0;
        }
        action = 0;
    }

    if( txtTranscripted.toLowerCase().indexOf( "criar item simples " ) == 0 && itemName == 0 ){
        action = 1;
        itemType = "Simples";   

        if( itemNameExists( txtTranscripted.slice( 19, txtTranscripted.length ) ) == 0 ){
            itemName = txtTranscripted.slice( 19, txtTranscripted.length );
            document.getElementById( 'speech' ).value = "Descrição";
            myVoice.text = "Descrição"; 
        }else{
            document.getElementById( 'speech' ).value = "O nome informado para o item já existe!";
            myVoice.text = "O nome informado para o item já existe!";

            itemType = 0;
            action = 0;
        } 
    } 

    //tarefa - action 2

    if( action == 2 && itemName != 0 && itemDescription != 0 ){
        if( compare( txtTranscripted, "nenhuma" ) != 0 ){
            itemDate = convertDate( txtTranscripted );
            if( itemDate != 1 ){  

                if( createItem( itemType, itemName, itemDescription, itemDate ) == 0 ){
                    document.getElementById( 'speech' ).value = "Item criado com sucesso!";
                    myVoice.text = "Item criado com sucesso!";
                    updateBD();
                } 
                else {
                    document.getElementById( 'speech' ).value = "Houve uma falha na criação do item!";
                    myVoice.text = "Houve uma falha na criação do item!";
                }
            } else {
                document.getElementById( 'speech' ).value = "A data informada é inválida!";
                myVoice.text = "A data informada é inválida!";
            }

            itemType = 0;
            itemName = 0;
            itemDescription = 0;    
            itemDate = 0;
        }else{

            if( createItem( itemType, itemName, itemDescription, null ) == 0 ){
                document.getElementById( 'speech' ).value = "Item criado com sucesso!";
                myVoice.text = "Item criado com sucesso!";
                updateBD();
            } 
            else{
                document.getElementById( 'speech' ).value = "Houve uma falha na criação do item!";
                myVoice.text = "Houve uma falha na criação do item!";
            } 

            itemType = 0;
            itemName = 0;
            itemDescription = 0;
        }
        action = 0;
    }

    if( action == 2 && itemName != 0 && itemDescription == 0 ){
        if( compare( txtTranscripted, "nenhuma" ) != 0 ) itemDescription = txtTranscripted; 
        else itemDescription = null;
        document.getElementById( 'speech' ).value = "Data";
        myVoice.text = "Data";
    }

    if( txtTranscripted.toLowerCase().indexOf( "criar tarefa " ) == 0 && itemName == 0 ){
        action = 2;
        itemType = "Tarefa"; 

        if( itemNameExists( txtTranscripted.slice( 13, txtTranscripted.length ) ) == 0 ){
            itemName = txtTranscripted.slice( 13, txtTranscripted.length );
            document.getElementById( 'speech' ).value = "Descrição";
            myVoice.text = "Descrição"; 
        }else{
            document.getElementById( 'speech' ).value = "O nome informado para o item já existe!";
            myVoice.text = "O nome informado para o item já existe!";
            itemType = 0;
            action = 0;
        } 
    } 

    //lembrete - action 3

    if( action == 3 && itemName != 0 && itemDescription != 0 ){
        if( compare( txtTranscripted, "nenhuma" ) != 0 ){
            itemDate = convertDate( txtTranscripted );
            if( itemDate != 1 ){

                if( createItem( itemType, itemName, itemDescription, itemDate ) == 0 ){
                    document.getElementById( 'speech' ).value = "Item criado com sucesso!";
                    myVoice.text = "Item criado com sucesso!";
                    updateBD();
                } 
                else {
                    document.getElementById( 'speech' ).value = "Houve uma falha na criação do item!";
                    myVoice.text = "Houve uma falha na criação do item!";
                }
            } else {
                document.getElementById( 'speech' ).value = "A data informada é inválida!";
                myVoice.text = "A data informada é inválida!";
            }
                    
            itemType = 0;
            itemName = 0;
            itemDescription = 0;    
            itemDate = 0;
        }else{

            if( createItem( itemType, itemName, itemDescription, null ) == 0 ){
                document.getElementById( 'speech' ).value = "Item criado com sucesso!";
                myVoice.text = "Item criado com sucesso!";
                updateBD();
            } 
            else{
                document.getElementById( 'speech' ).value = "Houve uma falha na criação do item!";
                myVoice.text = "Houve uma falha na criação do item!";
            } 

            itemType = 0;
            itemName = 0;
            itemDescription = 0;
        }
        action = 0;
    }

    if( action == 3 && itemName != 0 && itemDescription == 0 ){
        if( compare( txtTranscripted, "nenhuma" ) != 0 ) itemDescription = txtTranscripted; 
        else itemDescription = null;
        document.getElementById( 'speech' ).value = "Data";
            myVoice.text = "Data";
    }

    if( txtTranscripted.toLowerCase().indexOf( "criar lembrete " ) == 0 && itemName == 0 ){
        action = 3;
        itemType = "LEM";

        if( itemNameExists( txtTranscripted.slice( 15, txtTranscripted.length ) ) == 0 ){
            itemName = txtTranscripted.slice( 15, txtTranscripted.length );
            document.getElementById( 'speech' ).value = "Descrição";
            myVoice.text = "Descrição"; 
        }else{
            document.getElementById( 'speech' ).value = "O nome informado para o item já existe!";
            myVoice.text = "O nome informado para o item já existe!";
            itemType = 0;
            action = 0;
        } 
    } 

    //ALTERAR UMA PROPRIEDADE DE UM ITEM

    //nome - action 4

    if( action == 4 ){

        if( alterItemName( itemName, txtTranscripted ) == 0 ){
            document.getElementById( 'speech' ).value = "Item alterado com sucesso!";
            myVoice.text = "Item alterado com sucesso!";
            updateBD();
        }else{
            document.getElementById( 'speech' ).value = "Houve uma falha na alteração do item!";
            myVoice.text = "Houve uma falha na alteração do item!";
        }

        itemName = 0;
        action = 0;
    }

    if( txtTranscripted.toLowerCase().indexOf( "alterar nome do item " ) == 0 && itemName == 0 ){
        action = 4;

        if( itemNameExists( txtTranscripted.slice( 21, txtTranscripted.length ) ) == 1 ){
            itemName = txtTranscripted.slice( 21, txtTranscripted.length );
            document.getElementById( 'speech' ).value = "Qual o novo nome?";
            myVoice.text = "Qual o novo nome?"; 
        }else{
            document.getElementById( 'speech' ).value = "Não existe item com o nome informado!";
            myVoice.text = "Não existe item com o nome informado!";
            action = 0;
        } 
    } 
    
    //descrição - action 5

    if( action == 5 ){

        if( alterItemDescription( itemName, txtTranscripted ) == 0 ){
            document.getElementById( 'speech' ).value = "Item alterado com sucesso!";
            myVoice.text = "Item alterado com sucesso!";
            updateBD();
        }else{
            document.getElementById( 'speech' ).value = "Houve uma falha na alteração do item!";
            myVoice.text = "Houve uma falha na alteração do item!";
        }

        itemName = 0;
        action = 0;
    }

    if( txtTranscripted.toLowerCase().indexOf( "alterar descrição do item " ) == 0 && itemName == 0 ){
        action = 5;

        if( itemNameExists( txtTranscripted.slice( 26, txtTranscripted.length ) ) == 1 ){
            itemName = txtTranscripted.slice( 26, txtTranscripted.length );
            document.getElementById( 'speech' ).value = "Qual a nova descrição?";
            myVoice.text = "Qual a nova descrição?"; 
        }else{
            document.getElementById( 'speech' ).value = "Não existe item com o nome informado!";
            myVoice.text = "Não existe item com o nome informado!";

            action = 0;
        } 
    } 

    //data - action 6

    if( action == 6 ){
        itemDate = convertDate( txtTranscripted );
        if( itemDate != 1 ){

            if( alterItemDate( itemName, itemDate ) == 0 ){
                document.getElementById( 'speech' ).value = "Item alterado com sucesso!";
                myVoice.text = "Item alterado com sucesso!";
                updateBD();
            }else{
                document.getElementById( 'speech' ).value = "Houve uma falha na alteração do item!";
                myVoice.text = "Houve uma falha na alteração do item!";
            }
        }else{
            document.getElementById( 'speech' ).value = "A data informada é inválida!";
            myVoice.text = "A data informada é inválida!";
        }
                
        itemName = 0;
        itemDate = 0;
        action = 0;
    }

    if( txtTranscripted.toLowerCase().indexOf( "alterar data do item " ) == 0 && itemName == 0 ){
        action = 6;

        if( itemNameExists( txtTranscripted.slice( 21, txtTranscripted.length ) ) == 1 ){
            itemName = txtTranscripted.slice( 21, txtTranscripted.length );

            $.ajax({
                url : '/organizer/servletcontroller',
                type: "POST",
                data : {
                    function : "itemDatePropertyExists",
                    itemName : itemName
                },
                success : function(responseText) {
                    response = responseText;
                    alert(responseText);
                }
            });

            if( itemDatePropertyExists( itemName ) == 1 ){
                document.getElementById( 'speech' ).value = "Qual a nova data?";
                myVoice.text = "Qual a nova data?";
            } else {
                document.getElementById( 'speech' ).value = "O item informado não possui a propriedade data!";
                myVoice.text = "O item informado não possui a propriedade data!"; 

                itemName = 0;
                action = 0;
            }                
        }else{
            document.getElementById( 'speech' ).value = "Não existe item com o nome informado!";
            myVoice.text = "Não existe item com o nome informado!";

            action = 0;
        } 
    } 

    //EXCLUIR UM ITEM

    //pelo nome - action 7

    if( txtTranscripted.toLowerCase().indexOf( "excluir item " ) == 0 ){     

        if( itemNameExists( txtTranscripted.slice( 13, txtTranscripted.length ) ) == 1 ){

            if( deleteItem( txtTranscripted.slice( 13, txtTranscripted.length ) ) == 0 ){
                document.getElementById( 'speech' ).value = "Item excluído com sucesso!";
                myVoice.text = "Item excluído com sucesso!";
                updateBD();
            }else{
                document.getElementById( 'speech' ).value = "Houve uma falha na exclusão do item!";
                myVoice.text = "Houve uma falha na exclusão do item!";
            }
        }else{
            document.getElementById( 'speech' ).value = "Não existe item com o nome informado!";
            myVoice.text = "Não existe item com o nome informado!";
        }
    } 

    //pela tag - action 8

    if( txtTranscripted.toLowerCase().indexOf( "excluir itens com a tag " ) == 0 ){  

        if( tagNameExists( txtTranscripted.slice( 24, txtTranscripted.length ) ) == 1 ){

            var result =  deleteTagItems( txtTranscripted.slice( 24, txtTranscripted.length ) );
            if( result == 0 ){
                document.getElementById( 'speech' ).value = "Itens excluídos com sucesso!";
                myVoice.text = "Itens excluídos com sucesso!";
                updateBD();
            }else if( result == 1 ){
                document.getElementById( 'speech' ).value = "Não existe itens com a tag informada!";
                myVoice.text = "Não existe itens com a tag informada!";
            } else{
                document.getElementById( 'speech' ).value = "Houve uma falha na exclusão dos itens!";
                myVoice.text = "Houve uma falha na exclusão dos itens!";
            }
        }else{
            document.getElementById( 'speech' ).value = "Não existe tag com o nome informado!";
            myVoice.text = "Não existe tag com o nome informado!";
        }
    } 

    //PESQUISAR ITENS 

    //todos - action 9

    if( txtTranscripted.toLowerCase().indexOf( "pesquisar itens" ) == 0 ){   

        $.ajax({
            url : '/organizer/servletcontroller',
            type: "POST",
            data : {
                function : "searchItems",
            },
            success : function(responseText) {
                response = responseText;
                alert(responseText);
            }
        });

        var results = searchItems();

        if( results.length > 0 ){
            document.getElementById( 'speech' ).value = "A pesquisa retornou:";

            $.ajax({
                url : '/organizer/servletcontroller',
                type: "POST",
                data : {
                    function : "buildSearchResults",
                    results : results
                },
                success : function(responseText) {
                    response = responseText;
                    alert(responseText);
                }
            });

            document.getElementById( 'speech' ).value += buildSearchResults( results );     
            myVoice.text = "A pesquisa retornou:";
        }else{
            document.getElementById( 'speech' ).value = "Não houve nenhum resultado!";
            myVoice.text = "Não houve nenhum resultado!";
        }
    } 

    //pelo nome e descrição - action 10

    if( txtTranscripted.toLowerCase().indexOf( "pesquisar item usando " ) == 0 ){       
        var keyword = txtTranscripted.slice( 22, txtTranscripted.length );  

        $.ajax({
            url : '/organizer/servletcontroller',
            type: "POST",
            data : {
                function : "searchItem",
                keyword : keyword
            },
            success : function(responseText) {
                response = responseText;
                alert(responseText);
            }
        });

        var results = searchItem( keyword );    

        if( results.length > 0 ){
            document.getElementById( 'speech' ).value = "A pesquisa retornou:";

            $.ajax({
                url : '/organizer/servletcontroller',
                type: "POST",
                data : {
                    function : "buildSearchResults",
                    results : results
                },
                success : function(responseText) {
                    response = responseText;
                    alert(responseText);
                }
            });

            document.getElementById( 'speech' ).value += buildSearchResults( results );   
            myVoice.text = "A pesquisa retornou:"; 
        }else{
            document.getElementById( 'speech' ).value = "Não houve nenhum resultado!";
            myVoice.text = "Não houve nenhum resultado!";
        }
    } 

    //pelo tipo - action 11

    if( txtTranscripted.toLowerCase().indexOf( "pesquisar item pelo tipo usando " ) == 0 ){     
        var keyword = txtTranscripted.slice( 32, txtTranscripted.length ); 

        $.ajax({
            url : '/organizer/servletcontroller',
            type: "POST",
            data : {
                function : "searchItemByType",
                keyword : keyword
            },
            success : function(responseText) {
                response = responseText;
                alert(responseText);
            }
        });

        var results = searchItemByType( keyword );

        if( results.length > 0 ){
            document.getElementById( 'speech' ).value = "A pesquisa retornou:";

            $.ajax({
                url : '/organizer/servletcontroller',
                type: "POST",
                data : {
                    function : "buildSearchResults",
                    results : results
                },
                success : function(responseText) {
                    response = responseText;
                    alert(responseText);
                }
            });

            document.getElementById( 'speech' ).value += buildSearchResults( results );
            myVoice.text = "A pesquisa retornou:";       
        }else{
            document.getElementById( 'speech' ).value = "Não houve nenhum resultado!";
            myVoice.text = "Não houve nenhum resultado!";
        }
    } 

    //pelo nome - action 12

    if( txtTranscripted.toLowerCase().indexOf( "pesquisar item pelo nome usando " ) == 0 ){     
        var keyword = txtTranscripted.slice( 32, txtTranscripted.length );  

        $.ajax({
            url : '/organizer/servletcontroller',
            type: "POST",
            data : {
                function : "searchItemByName",
                keyword : keyword
            },
            success : function(responseText) {
                response = responseText;
                alert(responseText);
            }
        });

        var results = searchItemByName( keyword );

        if( results.length > 0 ){
            document.getElementById( 'speech' ).value = "A pesquisa retornou:";

            $.ajax({
                url : '/organizer/servletcontroller',
                type: "POST",
                data : {
                    function : "buildSearchResults",
                    results : results
                },
                success : function(responseText) {
                    response = responseText;
                    alert(responseText);
                }
            });

            document.getElementById( 'speech' ).value += buildSearchResults( results );
            myVoice.text = "A pesquisa retornou:";      
        }else{
            document.getElementById( 'speech' ).value = "Não houve nenhum resultado!";
            myVoice.text = "Não houve nenhum resultado!";
        }
    } 

    //pela descrição - action 13

    if( txtTranscripted.toLowerCase().indexOf( "pesquisar item pela descrição usando " ) == 0 ){      
        var keyword = txtTranscripted.slice( 37, txtTranscripted.length );    

        $.ajax({
            url : '/organizer/servletcontroller',
            type: "POST",
            data : {
                function : "searchItemByDescription",
                keyword : keyword
            },
            success : function(responseText) {
                response = responseText;
                alert(responseText);
            }
        });

        var results = searchItemByDescription( keyword );

        if( results.length > 0 ){
            document.getElementById( 'speech' ).value = "A pesquisa retornou:";

            $.ajax({
                url : '/organizer/servletcontroller',
                type: "POST",
                data : {
                    function : "buildSearchResults",
                    results : results
                },
                success : function(responseText) {
                    response = responseText;
                    alert(responseText);
                }
            });

            document.getElementById( 'speech' ).value += buildSearchResults( results );     
            myVoice.text = "A pesquisa retornou:";  
        }else{
            document.getElementById( 'speech' ).value = "Não houve nenhum resultado!";
            myVoice.text = "Não houve nenhum resultado!";
        }
    } 

    //pela data - action 14

    if( txtTranscripted.toLowerCase().indexOf( "pesquisar item pela data usando " ) == 0 ){     
        var keyword = convertDate( txtTranscripted.slice( 32, txtTranscripted.length ) );  

        $.ajax({
            url : '/organizer/servletcontroller',
            type: "POST",
            data : {
                function : "searchItemByDate",
                keyword : keyword
            },
            success : function(responseText) {
                response = responseText;
                alert(responseText);
            }
        });

        var results = searchItemByDate( keyword );
        if( results.length > 0 ){
            document.getElementById( 'speech' ).value = "A pesquisa retornou:";

            $.ajax({
                url : '/organizer/servletcontroller',
                type: "POST",
                data : {
                    function : "buildSearchResults",
                    results : results
                },
                success : function(responseText) {
                    response = responseText;
                    alert(responseText);
                }
            });

            document.getElementById( 'speech' ).value += buildSearchResults( results ); 
            myVoice.text = "A pesquisa retornou:";      
        }else{
            document.getElementById( 'speech' ).value = "Não houve nenhum resultado!";
            myVoice.text = "Não houve nenhum resultado!";
        }
    } 

    //pela tag - action 15

    if( txtTranscripted.toLowerCase().indexOf( "pesquisar item pela tag usando " ) == 0 ){      
        var keyword =  txtTranscripted.slice( 31, txtTranscripted.length );  

        $.ajax({
            url : '/organizer/servletcontroller',
            type: "POST",
            data : {
                function : "searchItemByTag",
                keyword : keyword
            },
            success : function(responseText) {
                response = responseText;
                alert(responseText);
            }
        });
                
        var results = searchItemByTag( keyword );
        if( results.length > 0 ){
            document.getElementById( 'speech' ).value = "A pesquisa retornou:";

            $.ajax({
                url : '/organizer/servletcontroller',
                type: "POST",
                data : {
                    function : "buildSearchResults",
                    results : results
                },
                success : function(responseText) {
                    response = responseText;
                    alert(responseText);
                }
            });

            document.getElementById( 'speech' ).value += buildSearchResults( results );
            myVoice.text = "A pesquisa retornou:";       
        }else{
            document.getElementById( 'speech' ).value = "Não houve nenhum resultado!";
            myVoice.text = "Não houve nenhum resultado!";
        }
    } 

    //CRIAR TAG - action 16

    if( txtTranscripted.toLowerCase().indexOf( "criar tag " ) == 0 ){

        if( tagNameExists( txtTranscripted.slice( 10, txtTranscripted.length ) ) == 0 ){

            if( createTag( txtTranscripted.slice( 10, txtTranscripted.length ) ) == 0 ){        
                    document.getElementById( 'speech' ).value = "Tag criada com sucesso!";
                    myVoice.text = "Tag criada com sucesso!";
                    updateBD();
            }else{ 
                document.getElementById( 'speech' ).value = "Houve uma falha na criação da tag!";
                myVoice.text = "Houve uma falha na criação da tag!";
            }
        }else{
            document.getElementById( 'speech' ).value = "O nome informado para a tag já existe!";
            myVoice.text = "O nome informado para a tag já existe!";            
        } 
    }

    //ALTERAR TAG - action 17

    if( action == 17 ){

        if( alterTagName( tagName, txtTranscripted ) == 0 ){
            document.getElementById( 'speech' ).value = "Tag alterada com sucesso!";
            myVoice.text = "Tag alterada com sucesso!";
            updateBD();
        }else{
            document.getElementById( 'speech' ).value = "Houve uma falha na alteração da tag!";
            myVoice.text = "Houve uma falha na alteração da tag!";
        }

        tagName = 0;
        action = 0;
    }

    if( txtTranscripted.toLowerCase().indexOf( "alterar nome da tag " ) == 0 ){
        action = 17;

        if( tagNameExists( txtTranscripted.slice( 20, txtTranscripted.length ) ) == 1 ){
            tagName = txtTranscripted.slice( 20, txtTranscripted.length );
            document.getElementById( 'speech' ).value = "Qual o novo nome?";
            myVoice.text = "Qual o novo nome?"; 
        }else{
            document.getElementById( 'speech' ).value = "Não existe tag com o nome informado!";
            myVoice.text = "Não existe tag com o nome informado!";
            action = 0;
        } 
    } 

    //EXCLUIR TAG - action 18

    if( action == 18 ){
        if( compare( txtTranscripted, "sim" ) == 0 ){

            if( deleteTag( tagName ) == 0 ){
                document.getElementById( 'speech' ).value = "Tag excluída com sucesso!";
                myVoice.text = "Tag excluída com sucesso!";
                updateBD();
            }else{
                document.getElementById( 'speech' ).value = "Houve uma falha na exclusão da tag!";
                myVoice.text = "Houve uma falha na exclusão da tag!";
            }
        } else if( compare( txtTranscripted, "não" ) == 0 ){
            document.getElementById( 'speech' ).value = "Exclusão cancelada!";
            myVoice.text = "Exclusão cancelada!";
        } else {
            document.getElementById( 'speech' ).value = "Resposta inválida!";
            myVoice.text = "Resposta inválida!";
        }

        tagName = 0;
        action = 0;
    }

    if( txtTranscripted.toLowerCase().indexOf( "excluir tag " ) == 0 ){ 
        action = 18;
        tagName = txtTranscripted.slice( 12, txtTranscripted.length );

        if( tagNameExists( tagName ) == 1 ){


            if( tagItemsExist( tagName ) == 0 ){


                if( deleteTag( tagName ) == 0 ){
                    document.getElementById( 'speech' ).value = "Tag excluída com sucesso!";
                    myVoice.text = "Tag excluída com sucesso!";
                    updateBD();

                    tagName = 0;
                    action = 0;
                }else{
                    document.getElementById( 'speech' ).value = "Houve uma falha na exclusão da tag!";
                    myVoice.text = "Houve uma falha na exclusão da tag!";

                    tagName = 0;
                    action = 0;
                }
            }else{
                document.getElementById( 'speech' ).value = "Há itens que utilizam essa tag. Ainda sim deseja excluí-la?";
                myVoice.text = "Há itens que utilizam essa tag. Ainda sim deseja excluí-la?";
            }
        }else{
            document.getElementById( 'speech' ).value = "Não existe tag com o nome informado!";
            myVoice.text = "Não existe tag com o nome informado!";
            tagName = 0;
            action = 0;
        }
    } 

    //ADICIONAR UMA TAG EM UM ITEM - action 19

    if( action == 19 ){

        if( tagNameExists( txtTranscripted ) == 1 ){

            if( itemTagExists( itemName, txtTranscripted ) == 0 ){

                if( addTagToItem( itemName, txtTranscripted ) == 0 ){
                    document.getElementById( 'speech' ).value = "Tag adicionada com sucesso!";
                    myVoice.text = "Tag adicionada com sucesso!";
                    updateBD();
                }else{
                    document.getElementById( 'speech' ).value = "Houve uma falha na adição da tag ao item!";
                    myVoice.text = "Houve uma falha na adição da tag ao item!";
                }
            }else{
                document.getElementById( 'speech' ).value = "O item já possui a tag informada!";
                myVoice.text = "O item já possui a tag informada!";
            }
        }else{
            document.getElementById( 'speech' ).value = "Não existe tag com o nome informado!";
                myVoice.text = "Não existe tag com o nome informado!";
        }
        
        itemName = 0;
        action = 0;
    }

    if( txtTranscripted.toLowerCase().indexOf( "adicionar tag ao item " ) == 0 && itemName == 0 ){
        action = 19;

        if( itemNameExists( txtTranscripted.slice( 22, txtTranscripted.length ) ) == 1 ){
            itemName = txtTranscripted.slice( 22, txtTranscripted.length );
            document.getElementById( 'speech' ).value = "Qual o nome da tag?";
            myVoice.text = "Qual o nome da tag?"; 
        }else{
            document.getElementById( 'speech' ).value = "Não existe item com o nome informado!";
            myVoice.text = "Não existe item com o nome informado!";
            action = 0;
        } 
    } 

    //REMOVER UMA TAG DE UM ITEM - action 20

    if( action == 20 ){

        if( tagNameExists( txtTranscripted ) == 1 ){

            if( itemTagExists( itemName, txtTranscripted ) == 1 ){

                if( removeTagFromItem( itemName, txtTranscripted ) == 0 ){
                    document.getElementById( 'speech' ).value = "Tag removida com sucesso!";
                    myVoice.text = "Tag removida com sucesso!";
                    updateBD();
                }else{
                    document.getElementById( 'speech' ).value = "Houve uma falha na remoção da tag do item!";
                    myVoice.text = "Houve uma falha na remoção da tag do item!";
                }
            }else{
                document.getElementById( 'speech' ).value = "O item não possui a tag informada!";
                myVoice.text = "O item não possui a tag informada!";
            }
        }else{
            document.getElementById( 'speech' ).value = "Não existe tag com o nome informado!";
                myVoice.text = "Não existe tag com o nome informado!";
        }
                
        itemName = 0;
        action = 0;
    }

    if( txtTranscripted.toLowerCase().indexOf( "remover tag do item " ) == 0 && itemName == 0 ){
        action = 20;

        if( itemNameExists( txtTranscripted.slice( 20, txtTranscripted.length ) ) == 1 ){
            itemName = txtTranscripted.slice( 20, txtTranscripted.length );
            document.getElementById( 'speech' ).value = "Qual o nome da tag?";
            myVoice.text = "Qual o nome da tag?"; 
        }else{
            document.getElementById( 'speech' ).value = "Não existe item com o nome informado!";
            myVoice.text = "Não existe item com o nome informado!";

            action = 0;
        } 
    } 

    //Solicita o speech do MAX
    //o setTimeout serve para fazer a função esperar um pouco para ser chamada já que muitas vezes ela é chamada antes do resultado da transcrição estar completo
    setTimeout( synth.speak( myVoice ), 250 ); 
}  

/* AUDIÇÃO E INTERPRETAÇÃO DE SONS - SPEECH-TO-TEXT */

//Testando se o browser tem suporte
window.SpeechRecognition = window.SpeechRecognition       ||
                           window.webkitSpeechRecognition ||
                           null;
if ( window.SpeechRecognition === null ) alert( "O browser não suporta a API" );

//Continua se a API tem suporte
else{
    //Inicia o controlador de reconhecimento
    var recognizer = new window.SpeechRecognition();
    
    //Cria referência para a textarea da transcrição
    var transcription = document.getElementById( 'transcription' );

    //Configura as características do reconhecedor
    recognizer.lang = "pt_BR"; //não precisa se setar a linguagem do HTML
    recognizer.continuous = false;
    recognizer.interimResults = true;

    //cria referência para o botão Interagir
    const btn1 = document.getElementById('btn_interact');
    const btn2 = document.getElementById('btn_manual');
    const btn3 = document.getElementById('btn_max');

    //verifica se o textarea foi clicado e assim preenchido
    var textareaIsFilled = 0;

    transcription.addEventListener('click', function func() {
        textareaIsFilled = 1;
    });

    //- Animações -

    //Configura o início da animação do botão Interagir e do o botão manual
    btn1.addEventListener('click', function Interact() { 
        btn1.classList.add('is-loading');
        btn2.classList.add('is-loading');
    });

    //Configura a ação do botão Manual
    btn2.addEventListener('click', function Manual() { 
        document.getElementById('rd_manual').checked = "true";
    });

    //Configura ação do botão MAX
    btn3.addEventListener('click', function MAX() { 
        document.getElementById('rd_max').checked = "true";
    });

    // - Início do reconhecimento -

    //Configura a ação do botão Interagir 
    btn1.addEventListener( 'click', function() {
        //se o textarea está preenchido e alguém tenta interagir o max 
        if( textareaIsFilled == 1 && transcription.value != '' ){
            sayMax();
            btn1.classList.remove('is-loading');
            btn2.classList.remove('is-loading');
            textareaIsFilled = 0;
        }
        else{ 
            //começa a reconhecer a voz
            try {
                recognizer.start();
            } catch( ex ) {
                // alert( 'Recognition error: ' + ex.message );
                document.getElementById( 'speech' ).value = "O reconhecimento já foi iniciado!";
            }
        }
    } );

    //Recebe os resultados
    recognizer.onresult = function( event ) {
        transcription.value = '';
        for ( var i = event.resultIndex; i < event.results.length; i++ ) {
            if ( event.results[i].isFinal ) {
                transcription.value = event.results[i][0].transcript;
            } else {
                transcription.value += event.results[i][0].transcript;
            }
        }
    };

    //Trata os erros
    // recognizer.onerror = function( event ) {
    //      alert( 'Recognition error: ' + event.message );
    // };

    //Solicita que o MAX fale quando o som terminar
    recognizer.onaudioend = function( event ){
        sayMax();
        btn1.classList.remove('is-loading');
        btn2.classList.remove('is-loading'); 
        textareaIsFilled = 0;
    }
}