/**
* Second Hand
* This is server to expose data for the second-hand marketplace application 
*
* OpenAPI spec version: 1.0.0
* Contact: rabhatt@syr.edu
*
* NOTE: This class is auto generated by the swagger code generator program.
* https://github.com/swagger-api/swagger-codegen.git
* Do not edit the class manually.
*/
package io.swagger.server.models

import java.io.Serializable


/**
 * 
 * @param itemId 
 * @param soldAtPrice 
 * @param soldOn 
 * @param soldToUserId 
 */
data class SoldInfo (
    val itemId: kotlin.Long? = null,
    val soldAtPrice: kotlin.Float? = null,
    val soldOn: String? = null,
    val soldToUserId: kotlin.String? = null
):Serializable {

}

