package p009rx.internal.util.unsafe;

import p009rx.internal.util.SuppressAnimalSniffer;
import p009rx.internal.util.atomic.LinkedQueueNode;

@SuppressAnimalSniffer
/* renamed from: rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef */
/* compiled from: BaseLinkedQueue */
abstract class BaseLinkedQueueProducerNodeRef<E> extends BaseLinkedQueuePad0<E> {
    protected static final long P_NODE_OFFSET = UnsafeAccess.addressOf(BaseLinkedQueueProducerNodeRef.class, "producerNode");
    protected LinkedQueueNode<E> producerNode;

    BaseLinkedQueueProducerNodeRef() {
    }

    /* access modifiers changed from: protected */
    public final void spProducerNode(LinkedQueueNode<E> linkedQueueNode) {
        this.producerNode = linkedQueueNode;
    }

    /* access modifiers changed from: protected */
    public final LinkedQueueNode<E> lvProducerNode() {
        return (LinkedQueueNode) UnsafeAccess.UNSAFE.getObjectVolatile(this, P_NODE_OFFSET);
    }

    /* access modifiers changed from: protected */
    public final LinkedQueueNode<E> lpProducerNode() {
        return this.producerNode;
    }
}
