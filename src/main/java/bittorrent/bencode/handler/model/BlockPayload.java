package bittorrent.bencode.handler.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BlockPayload {
    int index;
    int begin;
    byte[] block;
}
