using System;

namespace Dyehard
{
#if WINDOWS || XBOX
    public static class Program
    {
        static void Main(string[] args)
        {
            using (var game = new Game())
            {
                game.Run();
            }
        }
    }
#endif
}
