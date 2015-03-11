
package SpaceSmasher;

public class EmptyBlock extends Block
{
  protected EmptyBlock(int currentRow, int currentColumn)
  {
    setType(BlockType.EMPTY);

    row = currentRow;
    column = currentColumn;
    
    breakBlock();
  }
}
