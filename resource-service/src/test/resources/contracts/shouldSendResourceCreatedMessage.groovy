package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {

    description 'This contract verifies that the service sends a message when a resource is created.'
    label 'send_resource_created_message'

    input {
        triggeredBy('sendResourceCreatedMessage()')
    }
    outputMessage {
        sentTo 'resource-queue'
        body(
            id: "123"
        )
        headers {
            messagingContentType(applicationJson())
        }
    }
}