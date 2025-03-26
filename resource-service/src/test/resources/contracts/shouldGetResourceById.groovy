package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {

    description 'This contract verifies that the service returns a resource for a given ID.'

    request {
        method GET()
        url '/resources/123'
    }
    response {
        status OK()
        headers {
            contentType "audio/mpeg"
        }
        body "mockAudioData"
    }
}