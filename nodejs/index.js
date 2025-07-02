#!/usr/bin/env node

const { logger } = require("./utils/utils");

/**
 * Main entry point for the Symbiotic API Demo
 * This file can be used to run demos or serve as a starting point for custom implementations
 */

function showUsage() {
  console.log(`
Symbiotic API Demo - Node.js Version

Usage:
  node index.js [demo-name]

Available demos:
  upload          - File upload demo
  inpainting      - Inpainting demo
  outpainting     - Outpainting demo
  action-pose     - Make action pose demo
  fix-hand        - Fix hand demo
  pose-video      - Generate pose video demo

Examples:
  node index.js upload
  node index.js inpainting
  node index.js action-pose

Or use npm scripts:
  npm run demo:upload
  npm run demo:inpainting
  npm run demo:action-pose
`);
}

async function main() {
  const args = process.argv.slice(2);

  if (args.length === 0) {
    showUsage();
    return;
  }

  const demoName = args[0];

  try {
    switch (demoName) {
      case "upload":
        logger.info("Running file upload demo...");
        const { main: uploadDemo } = require("./demos/demo_upload_file");
        await uploadDemo();
        break;

      case "inpainting":
        logger.info("Running inpainting demo...");
        const { main: inpaintingDemo } = require("./demos/demo_inpainting");
        await inpaintingDemo();
        break;

      case "outpainting":
        logger.info("Running outpainting demo...");
        const { main: outpaintingDemo } = require("./demos/demo_outpainting");
        await outpaintingDemo();
        break;

      case "action-pose":
        logger.info("Running make action pose demo...");
        const {
          main: actionPoseDemo,
        } = require("./demos/demo_make_action_pose");
        await actionPoseDemo();
        break;

      case "fix-hand":
        logger.info("Running fix hand demo...");
        const { main: fixHandDemo } = require("./demos/demo_fix_hand");
        await fixHandDemo();
        break;

      case "pose-video":
        logger.info("Running generate pose video demo...");
        const {
          main: poseVideoDemo,
        } = require("./demos/demo_generate_pose_video");
        await poseVideoDemo();
        break;

      default:
        logger.error(`Unknown demo: ${demoName}`);
        showUsage();
        process.exit(1);
    }
  } catch (error) {
    logger.error(`Demo execution failed: ${error.message}`);
    process.exit(1);
  }
}

// Run the main function if this file is executed directly
if (require.main === module) {
  main();
}

module.exports = { main, showUsage };
