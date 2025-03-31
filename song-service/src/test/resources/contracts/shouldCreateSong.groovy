package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {

    description 'This contract verifies the creation of a song'

    request {
        method POST()
        url '/songs'
        headers {
            contentType(applicationJson())
        }
        body(
                name: "Mock song",
                artist: "Mock artist",
                album: "Mock album",
                length: "2:30",
                resourceId: 123L,
                year: "2022"
        )
    }
    response {
        status OK()
        body(
                id: 321L
        )
        headers {
            contentType(applicationJson())
        }
    }
}